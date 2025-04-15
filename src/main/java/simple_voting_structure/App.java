/**
 *  Class responsible to Start the application
 */
package simple_voting_structure;

import jade.wrapper.AgentController;
import jade.wrapper.AgentContainer;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;



/**
 * Class that set the main agent and it's actions
 */
public class App extends BaseAgent {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void setup() {
		
		loggerSetup();
		
		registerDF(this, "Manager", "manager");
		
		logger.log(Level.INFO, "Starting Agents...");
		
		logger.log(Level.INFO, "Creating voters...");

		ArrayList<String> votersName = new ArrayList<String>();
		
		Object[] args = getArguments();
		int votersQuorum = 0;
		if (args != null && args.length > 0) {
			votersQuorum =  Integer.parseInt(args[0].toString());
		}
		
		int votingStarter = (int) (Math.random() * votersQuorum);
		
		logger.log(Level.INFO, "Agent number " + votingStarter + " will request to the mediator!");
				
		for ( int i = 0; i < votersQuorum; ++i ) votersName.add("voter_" + i);

		try {
			// create agents on the same container of the creator agent
			AgentContainer container = (AgentContainer) getContainerController(); // get a container controller for creating
			
			votersName.forEach(voter -> {
				this.launchAgent(voter, "simple_voting_structure.Voter", null);	
				logger.log(Level.INFO, getLocalName() + " CREATED AND STARTED NEW VOTER: " + voter + " ON CONTAINER " + container.getName());
			});
		} catch (Exception any) {
			logger.log(Level.SEVERE, App.ANSI_RED + "ERROR WHILE CREATING AGENTS" + App.ANSI_RESET);
			any.printStackTrace();
		}
		
		String m1AgentName = "Mediator";
		launchAgent(m1AgentName, "simple_voting_structure.Mediator", votersName.toArray());
		
		logger.log(Level.INFO, "Agents started...");
		pauseSystem();
		
		// send them a message demanding start;
		logger.log(Level.INFO, "Starting system!");

		sendMessage(votersName.get(votingStarter), ACLMessage.INFORM, App.START);
	}
	

	private void pauseSystem() {
		try {
			logger.log(Level.WARNING, App.ANSI_YELLOW + "The system is paused -- this action is here only to let you activate the sniffer on the agents, if you want (see documentation)" + App.ANSI_RESET);
			logger.log(Level.WARNING, App.ANSI_YELLOW + "Press enter in the console to start the agents" + App.ANSI_RESET);
            System.in.read();
        } catch (IOException e) {
        	logger.log(Level.SEVERE, App.ANSI_RED + "ERROR STARTING THE SYSTEM" + App.ANSI_RESET);
            e.printStackTrace();
        }
	}

	private void launchAgent(String agentName, String className, Object[] args) {
		try {
			AgentContainer container = (AgentContainer)getContainerController(); // get a container controller for creating new agents
			AgentController newAgent = container.createNewAgent(agentName, className, args);
			newAgent.start();
		} catch (Exception e) {
			logger.log(Level.SEVERE, App.ANSI_RED + "ERROR WHILE LAUNCHING AGENTS" + App.ANSI_RESET);
			e.printStackTrace();
		}
	}
}
