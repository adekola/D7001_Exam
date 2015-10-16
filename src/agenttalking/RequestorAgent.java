/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agenttalking;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.core.*;
import jade.lang.acl.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import java.util.Vector;
import java.util.Date;
import jade.core.behaviours.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ameerah
 */
public class RequestorAgent extends Agent {

    private Vector adviceAgents = new Vector();

    protected void setup() {
//System.out.println("Hello world " +getAID().getLocalName());

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("SenderAgent");
        sd.setOwnership("Lab3");
        sd.addOntologies("SenderAgent");

        sd.setName(getName() + "-User-Service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
            doDelete();
        }

        /* try {
         DFService.deregister(this);
         } catch (FIPAException fe) {
         fe.printStackTrace();
         }
         */
        SendMessage requestAdvice = new SendMessage();
        addBehaviour(requestAdvice);
        ReceiveMessage receiveAdvice = new ReceiveMessage();
        addBehaviour(receiveAdvice);

    }

    public class SendMessage extends OneShotBehaviour {

        public void action() {

            try {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Life-Advice");
                template.addServices(sd);

                DFAgentDescription[] result = DFService.search(myAgent, template);
                if (result.length > 0) {

                    System.out.println("Found following agents");

                    for (int i = 0; i < result.length; ++i) {
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(result[0].getName());
                       // msg.addReceiver(new AID("R", AID.ISLOCALNAME));
                        msg.setLanguage("English");
                        msg.setContent("Life-Advice");

                        System.out.println("Hi I am the sender agent " + getAID().getLocalName());
                        send(msg);
                        System.out.println("Sender Agent Sent Message to::> R1 *****" + "\n"
                                + "The Content of My Message is::>" + msg.getContent());

                    }
                }
            } catch (FIPAException ex) {
                Logger.getLogger(RequestorAgent.class.getName()).log(Level.SEVERE, null, ex);
            }

            //}
             /*catch(FIPAException fe) {
             System.out.println("Sending Message to Receiver Failed");
             fe.printStackTrace();
             }*/
        }
    }

    public class ReceiveMessage extends CyclicBehaviour {

        private String Message_Performative;
        private String Message_Content;
        private String SenderName;

        public void action() {

           // System.out.println("Before building the ACL message in AgentTalking");
            ACLMessage msg = receive();
            if (msg != null) {

                Message_Performative = msg.getPerformative(msg.getPerformative());
                Message_Content = msg.getContent();
                SenderName = msg.getSender().getName();
                System.out.println(" ****I Received a Message***" + "\n"
                        + "The Sender Name is::>" + SenderName + "\n"
                        + "The Content of the Message is::> " + Message_Content + "\n"
                        + "::: And Performative is::> " + Message_Performative + "\n");
            } 
            else{
               // block();
            }
        }

    }

    @Override
    protected void takeDown() {
        doDelete(); //To change body of generated methods, choose Tools | Templates.
    }

}
