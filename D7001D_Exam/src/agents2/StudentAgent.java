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
import static java.lang.reflect.Array.set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adekola
 */
public class StudentAgent extends Agent {
    // 5- What does the following line of code do?

    private AID[] scientists;
//6- What is the role of setup() method?

    protected void setup() {
//7- Write a line of code and let your StudentAgent Introduce itself here:
        System.out.println("Hey I'm a StudentAgent with name: " + this.getName());
//8- Get your search field here as an command-line argument:
        Object[] args = getArguments();
        if (args != null) {
            String topic = (String) args[0];
            System.out.println(String.format("Student Agent here. I'm searching for: %s", topic));
        }
//9- What task does the following behavior perform
        addBehaviour(new TickerBehaviour(this, 1000) {
            protected void onTick() {
// Update the list of scientists
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("scientific-search"); //10- set an appropriate type here:
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    if (result != null & result.length > 0) {
                        scientists = new AID[result.length];
                        //11- Using a for loop, insert the entire names of running instances of ScientificAgent into the scientists list
                        for (int i = 0; i < result.length; i++) {
                            scientists[i] = result[i].getName();
                        }
                        System.out.println(String.format("It's Student Agent here. I found %s scientific agent(s)", result.length));
                    } else {
                        System.out.println("It's Student Agent here. I found no scientific agents (yet)");
                    }

                } catch (FIPAException ex) {
                    Logger.getLogger(StudentAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
