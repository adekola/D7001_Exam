
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
  

import jade.content.ContentElement;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Iterator;
import jade.core.*;
import jade.core.behaviours.*;
import jade.core.Runtime;import jade.core.Agent;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;


import jade.domain.mobility.*;
import jade.domain.FIPANames;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;


/**
This is an example of mobile agent. 
This class contains the resources used by the agent behaviours: the counter, 
the 
flag cntEnabled, and the list of visited locations. 
At the setup it creates a gui and adds behaviours to: get the list of
available locations from AMS, serve the incoming messages, and
to increment the counter. 
In particular, notice the usage of the two methods <code>beforeMove()</code> and
<code>afterMove()</code> to execute some application-specific tasks just before and just after
the agent migration takes effect.

Because this agent has a GUI, it extends the class GuiAgent that, in turn,
extends the class Agent. Being the GUI a different thread, the communication
between the agent and its GUI is based on event passing.
@see jade.gui.GuiAgent
@author Giovanni Caire - CSELT S.p.A
@version $Date: 2004-08-20 12:15:13 +0200 (ven, 20 ago 2004) $ $Revision: 5283 $
*/
public class MobileAgent extends GuiAgent {
  int     cnt;   // this is the counter
  public boolean cntEnabled;  // this flag indicates if counting is enabled
  transient protected MobileAgentGui gui;  // this is the gui
  Location nextSite;  // this variable holds the destination site

  // These constants are used by the Gui to post Events to the Agent
  public static final int EXIT = 1000;
  public static final int MOVE_EVENT = 1001;
  public static final int STOP_EVENT = 1002;
  public static final int CONTINUE_EVENT = 1003;
  public static final int REFRESH_EVENT = 1004;
  public static final int CLONE_EVENT = 1005;
  public static final int CREATE_EVENT = 1006;
  private int agentCount =0;

  // this vector contains the list of visited locations
  Vector visitedLocations = new Vector();

  public void setup() {
	  // register the SL0 content language
	  getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
	  // register the mobility ontology
	  getContentManager().registerOntology(MobilityOntology.getInstance());

	  // creates and shows the GUI
	  gui = new MobileAgentGui(this);
	  gui.setVisible(true); 

	  // get the list of available locations and show it in the GUI
	  addBehaviour(new GetAvailableLocationsBehaviour(this));

	  // initialize the counter and the flag
	  cnt = 0;
	  cntEnabled = true;

	  ///////////////////////
	  // Add agent behaviours to increment the counter and serve
	  // incoming messages
	  Behaviour b1 = new CounterBehaviour(this);
	  addBehaviour(b1);	
	  Behaviour b2 = new ServeIncomingMessagesBehaviour(this);
	  addBehaviour(b2);	
	}

	public void takeDown() {
	  if (gui!=null) {
            gui.dispose();
	    gui.setVisible(false);
	  }
          System.out.println(getLocalName()+" is now shutting down.");
	}

  /**
   * This method stops the counter by disabling the flag
   */
   void stopCounter(){
    cntEnabled = false;
   }

  /**
   * This method resume counting by enabling the flag
   */
   void continueCounter(){
     cntEnabled = true;
   }

  /**
   * This method displays the counter in the GUI
   */
   void displayCounter(){
     gui.displayCounter(cnt);
   }
  
   
protected void beforeClone() {
  System.out.println(getLocalName()+" is now cloning itself.");
}

protected void afterClone() {
  System.out.println(getLocalName()+" has cloned itself.");
  afterMove();
}
  /**
   * This method is executed just before moving the agent to another
   * location. It is automatically called by the JADE framework.
   * It disposes the GUI and prints a bye message on the standard output.
   */
	protected void beforeMove() 
	{
		gui.dispose();
		gui.setVisible(false);
		System.out.println(getLocalName()+" is now moving elsewhere.");
	}

  /**
   * This method is executed as soon as the agent arrives to the new 
   * destination.
   * It creates a new GUI and sets the list of visited locations and
   * the list of available locations (via the behaviour) in the GUI.
   */
   protected void afterMove() {
     System.out.println(getLocalName()+" is just arrived to this location.");
     // creates and shows the GUI
     gui = new MobileAgentGui(this);
     //if the migration is via RMA the variable nextSite can be null.
     if(nextSite != null)
     {
     	visitedLocations.addElement(nextSite);
      for (int i=0; i<visitedLocations.size(); i++)
        gui.addVisitedSite((Location)visitedLocations.elementAt(i));
     }
     gui.setVisible(true); 	
			
     // Register again SL0 content language and JADE mobility ontology,
     // since they don't migrate.
     getContentManager().registerLanguage(new SLCodec(), FIPANames.ContentLanguage.FIPA_SL0);
	 getContentManager().registerOntology(MobilityOntology.getInstance());
     // get the list of available locations from the AMS.
     // FIXME. This list might be stored in the Agent and migrates with it.
     addBehaviour(new GetAvailableLocationsBehaviour(this));
   }

  public void afterLoad() {
      afterClone();
  }

  public void beforeFreeze() {
      beforeMove();
  }

  public void afterThaw() {
      afterMove();
  }

  public void beforeReload() {
      beforeMove();
  }

  public void afterReload() {
      afterMove();
  }


	/////////////////////////////////
	// GUI HANDLING
		

	// AGENT OPERATIONS FOLLOWING GUI EVENTS
	protected void onGuiEvent(GuiEvent ev)
	{
		switch(ev.getType()) 
		{
		case EXIT:
			gui.dispose();
			gui = null;
			doDelete();
			break;
		case MOVE_EVENT:
      Iterator moveParameters = ev.getAllParameter();
      nextSite =(Location)moveParameters.next();
                    String agentName = (String) ev.getParameter(0);
                    System.out.println("The agent name is "+agentName);
        AID aid = new AID(agentName, AID.ISLOCALNAME);

//http://jade.tilab.com/pipermail/jade-develop/2009q2/013658.html
      MobileAgentDescription mad = new MobileAgentDescription();
            mad.setName(aid);
            mad.setDestination(nextSite);
            MoveAction ma = new MoveAction();
            ma.setMobileAgentDescription(mad);
            sendRequest(new jade.content.onto.basic.Action(aid, ma));
      
						break;
	        case CREATE_EVENT:
                    jade.wrapper.AgentController a = null;
            try {
                Object[] args = new Object[2];
                args[0] = getAID();
                String name = "Agent" +agentCount;
                agentCount++;
                
                jade.core.Runtime runtime1 = jade.core.Runtime.instance();
            			ProfileImpl p = new ProfileImpl(false);
				jade.wrapper.AgentContainer home=runtime1.createAgentContainer(p);  
                AgentController t2 = home.createNewAgent(name, getClass().getName(), args);
                t2.start();
                //agents.add(name);
                

            } catch (Exception ex) {
                System.out.println("Problem creating new agent");
            }
            
   	case STOP_EVENT:
		  stopCounter();
		  break;
		case CONTINUE_EVENT:
		  continueCounter();
		  break;
		case REFRESH_EVENT:
		  addBehaviour(new GetAvailableLocationsBehaviour(this));
		  break;
		}

	}
        void sendRequest(jade.content.onto.basic.Action action) {
// ---------------------------------

        ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
        request.setLanguage(new SLCodec().getName());
        request.setOntology(MobilityOntology.getInstance().getName());
        try {
            getContentManager().fillContent(request, (ContentElement) action);
            request.addReceiver(action.getActor());
            send(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        }
}

