package simple_voting_structure;

import java.util.ArrayList;
import java.util.logging.Level;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
// import jade.wrapper.StaleProxyException;

public class Mediator extends BaseAgent {

	private static final long serialVersionUID = 1L;

	private int answersCnt = 0;

	private int inpA, inpB;

	private AgentController p1 = null;
	private AgentController p2 = null;

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
					if (Mediator.ANSWER.equalsIgnoreCase(msg.getContent().split(" ")[0])) {
						// if an ANSWER to a greetings message is arrived
						// then send a THANKS message
						logger.log(Level.INFO, myAgent.getLocalName() + " RECEIVED ANSWER MESSAGE FROM " + msg.getSender().getLocalName());
						ACLMessage replyT = msg.createReply();
						replyT.setContent(Mediator.THANKS);
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

							replyW.setContent((((inpA + inpB) % 2 != 0) ? Mediator.ODD + " " + votersName.get(0)
									: Mediator.EVEN + " " + votersName.get(1)) + " WINNER!");
							replyW.addReceiver(new AID(votersName.get(0), AID.ISLOCALNAME));
							replyW.addReceiver(new AID(votersName.get(1), AID.ISLOCALNAME));
							myAgent.send(replyW);

							logger.log(Level.INFO, myAgent.getLocalName() + " SENT WINNER MESSAGE");
						}
					} else if (Mediator.START.equalsIgnoreCase(msg.getContent())) {
						// send them a message requesting for a number;
						ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
						msg2.setContent(Mediator.REQUEST);

						msg2.addReceiver(new AID(votersName.get(0), AID.ISLOCALNAME));
						msg2.addReceiver(new AID(votersName.get(1), AID.ISLOCALNAME));

						send(msg2);
						logger.log(Level.INFO, getLocalName() + " SENT REQUEST MESSAGE  TO " + votersName.get(0) + " AND " + votersName.get(1));
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
