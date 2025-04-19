package simple_voting_structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Map;

public class Mediator extends BaseAgent {

	private static final long serialVersionUID = 1L;
	private static final int MAX_VOTING_CODE = 9999;
	private static final int MIN_VOTING_VALUE = 1;
	private static final int MAX_VOTING_VALUE = 100;
	
	private int votingAnswer = 0;
	private int registeredQuorum = 0;
	private int totalQuorum = 0;
	
	private Hashtable<AID, Integer> votingLog;
	private ArrayList<AID> winners;

	@Override
	protected void setup() {

		logger.log(Level.INFO, "I'm the mediator!");
		this.registerDF(this, "Mediator", "mediator");
		
		addBehaviour(handleMessages());
	}
	
	@Override
	protected OneShotBehaviour handleInform ( ACLMessage msg ) {
		return new OneShotBehaviour(this) {
			private static final long serialVersionUID = 1L;

			public void action () {
				if (msg.getContent().startsWith(START)) {
					// send them a message requesting for a number;
					
					votingCode = votingCodeGenerator();
					
					votingLog = new Hashtable<>();
					winners = new ArrayList<>();
					
					setAns();

					registerDF(myAgent, Integer.toString(votingCode), Integer.toString(votingCode));
					
					registeredQuorum = 0;

					logger.log(Level.INFO, String.format("%s AGENT GENERATED VOTING WITH CODE %d!", getLocalName(), votingCode));
					
					ACLMessage msg2 = msg.createReply();

					msg2.setContent(String.format("VOTEID %d MINVALUE %d MAXVALUE %d", votingCode, MIN_VOTING_VALUE, MAX_VOTING_VALUE));

					send(msg2);
					logger.log(Level.INFO,  String.format("%s SENT VOTING CODE TO %s", getLocalName(), msg.getSender().getLocalName()));
				} else if ( msg.getContent().startsWith(INFORM) ) {
					String [] splittedMsg = msg.getContent().split(" ");
					
					totalQuorum = Integer.parseInt(splittedMsg[2]);
					
					logger.log(Level.INFO, String.format("EXPECTED QUORUM BY %s: %d VOTERS!", getLocalName(), totalQuorum));
				} else if ( msg.getContent().startsWith(REGISTERED) ) { 
					++registeredQuorum;
					
					if ( registeredQuorum == totalQuorum ) {
						logger.log(Level.INFO, "TOTAL QUORUM REACHED! REQUESTING VOTES!");
						
						requestVotes();
					}
				} else if ( msg.getContent().startsWith(VOTE) ) {
					int voteValue = Integer.parseInt(msg.getContent().split(" ")[3]);
					votingLog.put(msg.getSender(), voteValue);
					logger.log(Level.INFO, String.format("%s RECEIVED VOTE FROM %s!", getLocalName(), msg.getSender().getLocalName()));
					
					if ( votingLog.size() == registeredQuorum ) computeResults();
				} else {
					logger.log(Level.INFO, 
							String.format("%s RECEIVED AN UNEXPECTED MESSAGE FROM %s", getLocalName(), msg.getSender().getLocalName()));
				}
			}
		};
	}
	
	void computeResults() {
		int voteDist = 0;
		int minVoteDist = Integer.MAX_VALUE;
		
		Enumeration<AID> agents = votingLog.keys();
		
		while (agents.hasMoreElements()) {
			AID currAg = agents.nextElement();
			voteDist = calcVoteDistance(votingLog.get(currAg));
			
			if ( voteDist == minVoteDist ) winners.add(currAg);
			else if ( voteDist < minVoteDist ) {
				minVoteDist = voteDist;
				winners.clear();
				winners.add(currAg);
			}
		}
		
		System.out.println("Here are the winners: " + winners);
		System.out.println(String.format("CORRECT NUMBER %d\nWINNER VOTE VALUE: %d", votingAnswer, votingLog.get(winners.get(0))));
	}
	
	private int calcVoteDistance(int vote) {
		return Math.abs(votingAnswer - vote);
	}
	
	private void requestVotes() {
		try {
			ACLMessage requestVoteMsg = new ACLMessage(ACLMessage.REQUEST);
			requestVoteMsg.setContent(String.format("%s VOTE FOR %d", REQUEST, votingCode));
			
			ArrayList<DFAgentDescription> foundVotingParticipants = new ArrayList<>();
			String [] types = { Integer.toString(votingCode), "voter" };
			foundVotingParticipants = new ArrayList<DFAgentDescription>(
					Arrays.asList(searchAgentByType(types)));
			
			if ( foundVotingParticipants.size() != registeredQuorum ) {
				throw new Exception(String.format("FOUND VOTERS DIFFERS FROM REGISTERED QUORUM! (%d x %d)", 
						foundVotingParticipants.size(), registeredQuorum));
			}
			
			foundVotingParticipants.forEach(ag -> {
				requestVoteMsg.addReceiver(ag.getName());
			});
			
			send(requestVoteMsg);
			logger.log(Level.INFO, 
					String.format("%s REQUESTED A VOTE FOR ALL %d VOTERS!", getLocalName(), foundVotingParticipants.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int votingCodeGenerator () {
		int proposedCode;
		DFAgentDescription [] foundAgents;
		
		do {
			proposedCode = rand.nextInt(MAX_VOTING_CODE);
			
			foundAgents = searchAgentByType(Integer.toString(proposedCode));
		} while ( foundAgents.length > 0 );

		return proposedCode;
	}

	private void setAns(){
		votingAnswer = rand.nextInt(MIN_VOTING_VALUE, MAX_VOTING_VALUE + 1);
	}
}
