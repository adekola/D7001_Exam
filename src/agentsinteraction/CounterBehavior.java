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

import jade.core.*;
import jade.core.behaviours.*;

/**
The behaviour uses two resources, in particular the counter cnt
and the flag cntEnabled, of the agent object. 
It increments by one its value, displays it, blocks 
for two seconds, and repeats forever. 
@author Giovanni Caire - CSELT S.p.A
@version $Date: 2002-07-16 11:20:11 +0200 (mar, 16 lug 2002) $ $Revision: 3271 $
*/

class CounterBehaviour extends SimpleBehaviour
{
	CounterBehaviour(Agent a)
	{
		super(a);
	}

	public boolean done()
	{
		return false;
	}

	public void action()
	{
		// If counting is enabled, print current number and increment counter
		if ( ((MobileAgent) myAgent).cntEnabled )
		{
			((MobileAgent) myAgent).cnt++;
			((MobileAgent) myAgent).displayCounter();

		}
		
		// Block the behaviour for 2 seconds
		
		block(2000);

		return;
	}
}

