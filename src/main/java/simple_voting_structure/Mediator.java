package simple_voting_structure;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
// import jade.wrapper.StaleProxyException;

public class Mediator extends BaseAgent {

	private static final long serialVersionUID = 1L;

	private int answersCnt = 0;

	private int inpA, inpB;

	private AgentController p1 = null;
	private AgentController p2 = null;
	private AID initiator = null;

	@Override
	protected void setup() {

		System.out.println("I'm the mediator!");

		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			initiator = new AID((String) args[0], AID.ISLOCALNAME);
		}

		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			DFService.register(this, dfd);
			System.out.println(getLocalName() + " REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// create another two ThanksAgent
		String p1AgentName = "voter_1";
		String p2AgentName = "voter_2";

		try {
			// create agents t1 and t2 on the same container of the creator agent
			AgentContainer container = (AgentContainer) getContainerController(); // get a container controller for creating
																																						// new agents
			p1 = container.createNewAgent(p1AgentName, "simple_voting_structure.Voter", null);
			p1.start();
			p2 = container.createNewAgent(p2AgentName, "simple_voting_structure.Voter", null);
			p2.start();
			System.out.println(getLocalName() + " CREATED AND STARTED NEW VOTERS: " + p1AgentName + " AND " + p2AgentName
					+ " ON CONTAINER " + container.getContainerName());
		} catch (Exception any) {
			any.printStackTrace();
		}

		addBehaviour(new CyclicBehaviour(this) {
			public void action() {
				// listen if a greetings message arrives
				ACLMessage msg = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				if (msg != null) {
					if (MessageTypes.ANSWER.name().equalsIgnoreCase(msg.getContent().split(" ")[0])) {
						// if an ANSWER to a greetings message is arrived
						// then send a THANKS message
						System.out
								.println(myAgent.getLocalName() + " RECEIVED ANSWER MESSAGE FROM " + msg.getSender().getLocalName());
						// System.out.println(myAgent.getLocalName()+" " + msg.getContent());
						ACLMessage replyT = msg.createReply();
						replyT.setContent(MessageTypes.THANKS.name());
						myAgent.send(replyT);
						System.out.println(myAgent.getLocalName() + " SENT THANKS MESSAGE");

						if (msg.getSender().getLocalName().equals(p1AgentName)) {
							inpA = Integer.parseInt(msg.getContent().split(" ")[1]);
						} else {
							inpB = Integer.parseInt(msg.getContent().split(" ")[1]);
						}

						answersCnt++;
						if (answersCnt == 2) {
							ACLMessage replyW = new ACLMessage(ACLMessage.INFORM);

							replyW.setContent((((inpA + inpB) % 2 != 0) ? MessageTypes.ODD.name() + " " + p1AgentName
									: MessageTypes.EVEN.name() + " " + p2AgentName) + " WINNER!");
							replyW.addReceiver(new AID(p1AgentName, AID.ISLOCALNAME));
							replyW.addReceiver(new AID(p2AgentName, AID.ISLOCALNAME));
							myAgent.send(replyW);

							System.out.println(myAgent.getLocalName() + " SENT WINNER MESSAGE");
						}
					} else if (MessageTypes.START.name().equalsIgnoreCase(msg.getContent())) {
						// send them a message requesting for a number;
						ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
						msg2.setContent(MessageTypes.REQUEST.name());

						msg2.addReceiver(new AID(p1AgentName, AID.ISLOCALNAME));
						msg2.addReceiver(new AID(p2AgentName, AID.ISLOCALNAME));

						send(msg2);
						System.out.println(getLocalName() + " SENT REQUEST MESSAGE  TO " + p1AgentName + " AND " + p2AgentName);
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
