/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents1;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.lang.acl.*;

/**
 *
 * @author adekola
 */
public class Creature extends Agent {

    public void setup() {
        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(" - "
                            + myAgent.getLocalName() + " received: "
                            + msg.getContent());
                    
                    // 14-create an ACL message called "reply"
                    ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                    String replyString = "Hi Yourself";
                    // 15-set its performative to INFORM
                    reply.setContent(replyString);
                     // 16-set appropriate message content as mentionedin the question
                     // 17-send the message to the sender
                    reply.addReceiver(msg.getSender());
                    send(reply);
                    System.out.println("My Reply was: "+  replyString);
                }
                block();
            }
        });
    }

}
