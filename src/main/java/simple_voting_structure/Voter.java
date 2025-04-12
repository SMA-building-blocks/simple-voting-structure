package simple_voting_structure;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Voter extends BaseAgent {

	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		System.out.println("I'm voter: " + this.getLocalName() + "!");
		
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
						System.out
								.println(myAgent.getLocalName() + " RECEIVED REQUEST MESSAGE FROM " + msg.getSender().getLocalName());
						ACLMessage reply = msg.createReply();
						reply.setContent(Voter.ANSWER + " " + (Min + (int) (Math.random() * ((Max - Min) + 1))));
						myAgent.send(reply);
						System.out.println(myAgent.getLocalName() + " SENT ANSWER MESSAGE");
					} else if (Voter.THANKS.equalsIgnoreCase(msg.getContent())) {
						System.out
								.println(myAgent.getLocalName() + " RECEIVED THANKS MESSAGE FROM " + msg.getSender().getLocalName());
					} else if (Voter.ODD.equalsIgnoreCase(msg.getContent().split(" ")[0])
							|| Voter.EVEN.equalsIgnoreCase(msg.getContent().split(" ")[0])) {
						System.out
								.println(myAgent.getLocalName() + " RECEIVED RESULTS MESSAGE FROM " + msg.getSender().getLocalName());
						System.out.println(myAgent.getLocalName() + " " + msg.getContent());
					} else {
						System.out.println(
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
