/**
 *  Class responsible to Start the application
 */
package simple_voting_structure;

import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.AgentContainer;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

/**
 * Class that set the main agent and it's actions
 */
public class App extends BaseAgent {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void setup() {
		
		System.out.println("Ola Mundo! ");
		System.out.println("Meu nome: " + getLocalName());
		
		registerDF(this, "Manager", "manager");
		
		System.out.println("Starting Agents...");
		
		String m1AgentName = "Mediator";
		launchAgent(m1AgentName, "simple_voting_structure.Mediator", null);
		System.out.println("Agents started...");
		
		pauseSystem();
		
		// send them a message demanding start;
		System.out.println("Starting system!");		
		sendMessage(m1AgentName, ACLMessage.INFORM, MessageTypes.START.name());
	}

	private void pauseSystem() {
		try {
            System.out.println("The system is paused -- this action is only here to let you activate the sniffer on the agents, if you want (see documentation)");
            System.out.println("Press enter in the console to start the agents");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void launchAgent(String agentName, String className, Object[] args) {
		try {
			AgentContainer container = (AgentContainer)getContainerController(); // get a container controller for creating new agents
			AgentController newAgent = container.createNewAgent(agentName, className, args);
			newAgent.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
