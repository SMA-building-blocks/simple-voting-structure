package simple_voting_structure;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
// import jade.wrapper.StaleProxyException;

public abstract class BaseAgent extends Agent {
	
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
	
	protected void setup () {}
	
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
