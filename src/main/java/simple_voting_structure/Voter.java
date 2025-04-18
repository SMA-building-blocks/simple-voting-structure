package simple_voting_structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Voter extends BaseAgent {

	private static final long serialVersionUID = 1L;
	
	private int minVotingValue;
	private int maxVotingValue;

	@Override
	protected void setup() {
		logger.log(Level.INFO, "I'm voter: " + this.getLocalName() + "!");
		
		this.registerDF(this, "Voter", "voter");

		addBehaviour(new HandleMessages());
	}
	
	private class HandleMessages extends CyclicBehaviour {
		private static final long serialVersionUID = 1L;

		public void action() {
			ACLMessage msg = receive();
			
			if ( msg == null ) block();
			else {
				switch ( msg.getPerformative() ) {
				case ACLMessage.INFORM:
					addBehaviour(new HandleInform(msg));
				default:
					logger.log(Level.INFO, 
							String.format("%s RECEIVED UNEXPECTED MESSAGE PERFORMATIVE FROM %s", getLocalName(), msg.getSender().getLocalName()));
				}
			}
		}
	}
	
	private class HandleInform extends OneShotBehaviour {
		private static final long serialVersionUID = 1L;
		private ACLMessage msg;
		
		private HandleInform (ACLMessage  msg) {
			this.msg = msg;
		}
		
		public void action () {
			if (REQUEST.equalsIgnoreCase(msg.getContent())) {
				final int Max = 10;
				final int Min = 1;

				// if a greetings message is arrived then send an ANSWER
				logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED REQUEST MESSAGE FROM " + msg.getSender().getLocalName());
				ACLMessage reply = msg.createReply();
				reply.setContent(ANSWER + " " + (Min + (int) (Math.random() * ((Max - Min) + 1))));
				myAgent.send(reply);
				logger.log(Level.INFO, myAgent.getLocalName() + " SENT ANSWER MESSAGE");
			} else if (START.equalsIgnoreCase(msg.getContent())) {
				ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
				msg2.setContent(START);
				
				DFAgentDescription [] foundAgents = searchAgentByType("mediator");
				
				try {
					AID foundMediator = null;
					if ( foundAgents.length > 0 ) {
						foundMediator = foundAgents[0].getName();
						
						msg2.addReceiver(foundMediator);
						
						send(msg2);
						logger.log(Level.INFO, String.format("%s SENT START MESSAGE TO %s", getLocalName(), foundMediator.getLocalName()));
					}
				} catch ( Exception any ) {
					logger.log(Level.SEVERE, ANSI_RED + "ERROR WHILE SENDING MESSAGE" + ANSI_RESET);
					any.printStackTrace();
				}
			} else if (THANKS.equalsIgnoreCase(msg.getContent())) {
				logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED THANKS MESSAGE FROM " + msg.getSender().getLocalName());
			} else if (ODD.equalsIgnoreCase(msg.getContent().split(" ")[0])
					|| EVEN.equalsIgnoreCase(msg.getContent().split(" ")[0])) {
				logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED RESULTS MESSAGE FROM " + msg.getSender().getLocalName());
				logger.log(Level.INFO, myAgent.getLocalName() + " " + msg.getContent());
			} else if (msg.getContent().startsWith(VOTEID)) {
				logger.log(Level.INFO, 
						String.format("RECEIVED VOTING STRUCTURE FROM %s: %s", msg.getSender().getLocalName(), msg.getContent()));
				
				String [] splittedMsg = msg.getContent().split(" ");
				
				votingCode = Integer.parseInt(splittedMsg[1]);
				minVotingValue = Integer.parseInt(splittedMsg[3]);
				maxVotingValue = Integer.parseInt(splittedMsg[5]);
				
				registerDF(myAgent, Integer.toString(votingCode), Integer.toString(votingCode));
				
				informVotingRegistration();
				
				ArrayList<DFAgentDescription> foundAgents = new ArrayList<DFAgentDescription>(
						Arrays.asList(searchAgentByType("voter")));
				
				ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
				msg2.setContent(String.format("%s %s", INVITE, msg.getContent()));
				
				foundAgents.forEach(ag -> {
					if ( !ag.getName().equals(myAgent.getAID())  ) {
						msg2.addReceiver(ag.getName());
					}
				});
				
				logger.log(Level.INFO, 
						String.format("%s RECEIVED UNEXPECTED MESSAGE FROM %s", getLocalName(), msg.getSender().getLocalName()));	ACLMessage reply = msg.createReply();
				reply.setContent(String.format("%s QUORUM %d", INFORM, foundAgents.size()));
				myAgent.send(reply);
				
				send(msg2);
				logger.log(Level.INFO, String.format("%s SENT INVITE TO VOTERS!", getLocalName()));
				
				
			} else if (msg.getContent().startsWith(INVITE)) {
				logger.log(Level.INFO, 
						String.format("RECEIVED VOTING STRUCTURE FROM %s: %s", msg.getSender().getLocalName(), msg.getContent()));		
				
				String [] splittedMsg = msg.getContent().split(" ");

				votingCode = Integer.parseInt(splittedMsg[2]);
				minVotingValue = Integer.parseInt(splittedMsg[4]);
				maxVotingValue = Integer.parseInt(splittedMsg[6]);
				
				registerDF(myAgent, Integer.toString(votingCode), Integer.toString(votingCode));
				
				informVotingRegistration();
			} else {
				logger.log(Level.INFO, 
						String.format("%s RECEIVED UNEXPECTED MESSAGE FROM %s", getLocalName(), msg.getSender().getLocalName()));
			}
		}
	}
	
	private void informVotingRegistration() {
		ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
		informMsg.setContent(String.format("%s IN %d", REGISTERED, votingCode));
		
		ArrayList<DFAgentDescription> foundVotingParticipants = new ArrayList<>();
		String [] types = { Integer.toString(votingCode), "mediator" };
		foundVotingParticipants = new ArrayList<DFAgentDescription>(
				Arrays.asList(searchAgentByType(types)));
		
		foundVotingParticipants.forEach(ag -> {
			informMsg.addReceiver(ag.getName());
		});
		
		send(informMsg);
		logger.log(Level.INFO, String.format("%s INFORMED VOTING REGISTRATION TO MEDIATOR!", getLocalName()));
	}
}
