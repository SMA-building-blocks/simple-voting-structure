package simple_voting_structure;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
// import jade.wrapper.StaleProxyException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public abstract class BaseAgent extends Agent {
	
	private static final long serialVersionUID = 1L;
	
	public static final String ODD = "ODD";
	public static final String EVEN = "EVEN";
	public static final String REQUEST = "REQUEST";
	public static final String ANSWER = "ANSWER";
	public static final String THANKS = "THANKS";
	public static final String START = "START";
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\033[1;93m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	
	protected static final Logger logger = Logger.getLogger(BaseAgent.class.getName());
	
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
			logger.log(Level.INFO, getLocalName()+" REGISTERED WITH THE DF" );
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
	
	protected DFAgentDescription[] searchAgentByType (String type) {
		DFAgentDescription search = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		DFAgentDescription [] foundAgents = null;
		
		sd.setType(type);
		search.addServices(sd);
		
		try {
			foundAgents = DFService.search(this, search);
		} catch ( Exception any ) {
			any.printStackTrace();
		}
		
		return foundAgents;
	}
	
	protected void takeDown() {
		// Deregister with the DF
		try {
			DFService.deregister(this);
			logger.log(Level.INFO,getLocalName()+" DEREGISTERED WITH THE DF" );

		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
	
	protected void loggerSetup() {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new Formatter());
		logger.setUseParentHandlers(false);
		logger.addHandler(handler);
	}
	

}
