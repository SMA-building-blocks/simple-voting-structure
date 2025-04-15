package simple_voting_structure;

import java.util.logging.Level;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Voter extends BaseAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		logger.log(Level.INFO, "I'm voter: " + this.getLocalName() + "!");
		
		this.registerDF(this, "Voter", "voter");

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// listen if a greetings message arrives
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {
					if (Voter.REQUEST.equalsIgnoreCase(msg.getContent())) {
						final int Max = 10;
						final int Min = 1;

						// if a greetings message is arrived then send an ANSWER
						logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED REQUEST MESSAGE FROM " + msg.getSender().getLocalName());
						ACLMessage reply = msg.createReply();
						reply.setContent(Voter.ANSWER + " " + (Min + (int) (Math.random() * ((Max - Min) + 1))));
						myAgent.send(reply);
						logger.log(Level.INFO, myAgent.getLocalName() + " SENT ANSWER MESSAGE");
					} else if (Voter.START.equalsIgnoreCase(msg.getContent())) {
						ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
						msg2.setContent(Voter.START);
						
						DFAgentDescription [] foundAgents = searchAgentByType("mediator");
						
						try {
							AID foundMediator = null;
							if ( foundAgents.length > 0 ) {
								foundMediator = foundAgents[0].getName();
								
								msg2.addReceiver(foundMediator);
								
								send(msg2);
								logger.log(Level.INFO, getLocalName() + " SENT START MESSAGE  TO " + foundMediator);
							}
						} catch ( Exception any ) {
							logger.log(Level.SEVERE, App.ANSI_RED + "ERROR WHILE SENDING MESSAGE" + App.ANSI_RESET);
							any.printStackTrace();
						}
					} else if (Voter.THANKS.equalsIgnoreCase(msg.getContent())) {
						logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED THANKS MESSAGE FROM " + msg.getSender().getLocalName());
					} else if (Voter.ODD.equalsIgnoreCase(msg.getContent().split(" ")[0])
							|| Voter.EVEN.equalsIgnoreCase(msg.getContent().split(" ")[0])) {
						logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED RESULTS MESSAGE FROM " + msg.getSender().getLocalName());
						logger.log(Level.INFO, myAgent.getLocalName() + " " + msg.getContent());
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
}
