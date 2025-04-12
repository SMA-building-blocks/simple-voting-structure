package simple_voting_structure;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
// import jade.wrapper.StaleProxyException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public abstract class BaseAgent extends Agent {
	
	private static final long serialVersionUID = 1L;

	public enum MessageTypes {
		ODD("ODD"), EVEN("EVEN"), REQUEST("REQUEST"), ANSWER("ANSWER"), THANKS("THANKS"), START("START");
		
		private final String selMsgType;
		
		MessageTypes(String selected) {
			selMsgType = selected;
		}
		
		public String getMessageType() {
			return selMsgType;
		}
	};
	
	@Override
	protected void setup() {}
	
	protected void registerDF(Agent regAgent, String sdName, String sdType) {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			
			ServiceDescription sd = new ServiceDescription();
			sd.setType(sdType);
			sd.setName(sdName);
			
			dfd.addServices(sd);
			
			DFService.register(regAgent, dfd);
			System.out.println(getLocalName()+" REGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	protected void sendMessage(String agentName, int performative, String content) {
		ACLMessage msg = new ACLMessage(performative);
		msg.setContent(content);
		msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));
		send(msg);
	}
	
	protected void takeDown() {
		// Deregister with the DF
		try {
			DFService.deregister(this);
			System.out.println(getLocalName()+" DEREGISTERED WITH THE DF");
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	
	

}
