/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents2;

import jade.core.Agent;
import jade.core.behaviours.*;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.lang.acl.*;

/**
 *
 * @author adekola
 */
public class ScientistAgent extends Agent {

    protected void setup() {

//1- What does the following line of codes do
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
//2- Set the type of your instance of ServiceDescription class to “scientific-search” here
        sd.setType("scientific-search");
//3- Set the name of your instance of ServiceDescription class to “scientist-agent” here
        sd.setName("scientist-agent");
        dfd.addServices(sd);
        try {
//4- Register your instance of DFAgentDescription here
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            System.err.println(fe.getMessage());
        }
    }
}
