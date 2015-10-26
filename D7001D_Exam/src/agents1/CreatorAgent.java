/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents1;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.lang.acl.*;
import jade.wrapper.StaleProxyException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adekola
 */
public class CreatorAgent extends Agent {

    String name = "Bender-The-Offender";
    AID Bender = new AID(name); //since this is not a golbally unique ID
    
    public void setup(){
        AgentContainer c = this.getContainerController();
        
        try{
            
            AgentController a  = c.createNewAgent(Bender.getName(), Creature.class.getName(), null);
            a.start();
            Bender.setName(a.getName());
            System.out.println("+++ Created: " + Bender);
            
            addBehaviour(new CyclicBehaviour() {

                int n = 0;
                @Override
                public void action() {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContent("Hi");
                    msg.addReceiver(Bender);
                    System.out.println("+++ Sending: " + n);
                    //10- Send the Message
                    send(msg);
                    //block(1000);
                }
                
                //public boolean done(){
                //    return false;
                //}
            });
        } catch (StaleProxyException ex) {
            Logger.getLogger(CreatorAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}