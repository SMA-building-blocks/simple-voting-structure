package simple_voting_structure;

import java.util.ArrayList;
import java.util.logging.Level;

import FIPA.stringsHelper;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Mediator extends BaseAgent {

	private static final long serialVersionUID = 1L;
	private static final int MAX_VOTING_CODE = 9999;
	private static final int MIN_VOTING_VALUE = 1;
	private static final int MAX_VOTING_VALUE = 100;
	
	private int ans;
	private int answersCnt = 0;
	private int inpA;
	private int inpB;

	@Override
	protected void setup() {

		logger.log(Level.INFO, "I'm the mediator!");

		Object[] args = getArguments();
		ArrayList<String> votersName = new ArrayList<String>();
		
		if (args != null && args.length > 0) {
			for (Object voter : args) {
				votersName.add(voter.toString());
			}
		}
		
		this.registerDF(this, "Mediator", "mediator");

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// listen if a greetings message arrives
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {
					if (ANSWER.equalsIgnoreCase(msg.getContent().split(" ")[0])) {
						// if an ANSWER to a greetings message is arrived
						// then send a THANKS message
						logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED ANSWER MESSAGE FROM " + msg.getSender().getLocalName());
						ACLMessage replyT = msg.createReply();
						replyT.setContent(THANKS);
						myAgent.send(replyT);
						logger.log(Level.INFO, myAgent.getLocalName() + " SENT THANKS MESSAGE");

						if (msg.getSender().getLocalName().equals(votersName.get(0))) {
							inpA = Integer.parseInt(msg.getContent().split(" ")[1]);
						} else {
							inpB = Integer.parseInt(msg.getContent().split(" ")[1]);
						}

						answersCnt++;
						if (answersCnt == 2) {
							ACLMessage replyW = new ACLMessage(ACLMessage.INFORM);

							replyW.setContent((((inpA + inpB) % 2 != 0) ? ODD + " " + votersName.get(0)
									: EVEN + " " + votersName.get(1)) + " WINNER!");
							replyW.addReceiver(new AID(votersName.get(0), AID.ISLOCALNAME));
							replyW.addReceiver(new AID(votersName.get(1), AID.ISLOCALNAME));
							myAgent.send(replyW);

							logger.log(Level.INFO, myAgent.getLocalName() + " SENT WINNER MESSAGE");
						}
					} else if (START.equalsIgnoreCase(msg.getContent())) {
						// send them a message requesting for a number;
						
						votingCode = votingCodeGenerator();
						
						setAns();

						registerDF(myAgent, Integer.toString(votingCode), Integer.toString(votingCode));

						logger.log(Level.INFO, String.format("%s AGENT GENERATED VOTING WITH CODE %d!", getLocalName(), votingCode));
						
						ACLMessage msg2 = msg.createReply();

						msg2.setContent(String.format("VOTEID %d MINVALUE %d MAXVALUE %d", votingCode, MIN_VOTING_VALUE, MAX_VOTING_VALUE));

						send(msg2);
						logger.log(Level.INFO, getLocalName() + " SENT VOTING CODE TO " + msg.getSender().getLocalName());
					
					} else {
						logger.log(Level.INFO, 
								myAgent.getLocalName() + " Unexpected message received from " + msg.getSender().getLocalName());
					}
				} else {
					// if no message is arrived, block the behaviour
					block();
				}
			}
		});
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
		ans = rand.nextInt(MIN_VOTING_VALUE, MAX_VOTING_VALUE);
	}
}
