/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentsinteraction;

/**
 *
 * @author ameerah
 */

import jade.core.Agent;
import jade.core.Location;
import jade.core.ProfileImpl;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

import java.util.StringTokenizer;

/**
This behaviour of the Agent serves all the received messages. In particular,
the following expressions are accepted as content of "request" messages:
- (move <destination>)  to move the Agent to another container. Example (move Front-End) or
(move (:location (:name Container-1) (:transport-protocol JADE-IPMT) (:transport-address IOR:0000...) ))
- (exit) to request the agent to exit
- (stop) to stop the counter
- (continue) to continue counting
@author Giovanni Caire - CSELT S.p.A
@version $Date: 2008-10-09 14:04:02 +0200 (gio, 09 ott 2008) $ $Revision: 6051 $
*/
class ServeIncomingMessagesBehaviour extends SimpleBehaviour
{
	ServeIncomingMessagesBehaviour(Agent a)
	{
		super(a);
	}

	public boolean done()
	{
		return false;
	}

	public void action()
	{
		ACLMessage msg;
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

		// Get a message from the queue or wait for a new one if queue is empty
		msg = myAgent.receive(mt);
		if (msg == null) {
			block();
		 	return;
		}
		else {
			String replySentence = "";

			// Get action to perform
			//String s = msg.getContent().
			StringTokenizer st = new StringTokenizer(msg.getContent(), " ()\t\n\r\f");
			String action = (st.nextToken()).toLowerCase();
			// EXIT
			if      (action.equals("exit"))
			{
				System.out.println("They requested me to exit (Sob!)");
				// Set reply sentence
				replySentence = "\"OK exiting\"";
				myAgent.doDelete();
			}
			// STOP COUNTING
			else if (action.equals("stop"))
			{
				System.out.println("They requested me to stop counting");
				((MobileAgent) myAgent).stopCounter();
				// Set reply sentence
				replySentence = "\"OK stopping\"";
			} 				
			// CONTINUE COUNTING
			else if (action.equals("continue"))
			{
				System.out.println("They requested me to continue counting");
				((MobileAgent) myAgent).continueCounter();
				// Set reply sentence
				replySentence = "\"OK continuing\"";
			} 
			// MOVE TO ANOTHER LOCATION				
			else if (action.equals("move"))
			{
			   /* String destination = st.nextToken();
			    System.out.println();
			    Location dest = new jade.core.ContainerID(destination, null);
			    System.out.println("They requested me to go to " + destination);
				// Set reply sentence
				replySentence = "\"OK moving to " + destination+" \"";
				// Prepare to move
				((MobileAgent)myAgent).nextSite = dest;
				myAgent.doMove(dest);*/
			}
			// CLONE TO ANOTHER LOCATION	
                       /* else if (action.equals("create")){
                        jade.core.Runtime rt = jade.core.Runtime.instance();
			ProfileImpl p = new ProfileImpl(false);
                        try {
				// Create a new non-main container, connecting to the default
				// main container (i.e. on this host, port 1099)
			AgentContainer	ac = rt.createAgentContainer(p);
				// create a new agent
				AgentController t2 = ac.createNewAgent("Agent",getClass().getName(),new Object[0]);
				// fire-up the agent
				t2.start();
				//System.out.println(getLocalName()+" CREATED AND STARTED NEW THANKSAGENT:"+t2AgentName + " ON CONTAINER "+ac.getContainerName());
			} catch (Exception e2) {
				e2.printStackTrace();
			}

                       
                       }*/
			/*else if (action.equals("clone"))
			{
			    String destination = st.nextToken();
			    System.out.println();
			    Location dest = new jade.core.ContainerID(destination, null);
			    System.out.println("They requested me to clone myself to " + destination);
				// Set reply sentence
				replySentence = "\"OK cloning to " + destination+" \"";
				// Prepare to move
				((MobileAgent)myAgent).nextSite = dest;
				myAgent.doClone(dest, "clone"+((MobileAgent)myAgent).cnt+"of"+myAgent.getName());
			}*/
			// SAY THE CURRENT LOCATION 
			else if (action.equals("where-are-you"))
			{
			    System.out.println();
			    Location current = myAgent.here();
			    System.out.println("Currently I am running on "+current.getName());
				// Set reply sentence
				replySentence = current.getName();
			}

			// Reply
			ACLMessage replyMsg = msg.createReply();
			replyMsg.setPerformative(ACLMessage.INFORM);
			replyMsg.setContent(replySentence);
			myAgent.send(replyMsg);
		}

		return;
	}
}

