package uncertainty;

import java.util.List;

import wumpus.GridPos;
import wumpus.ObservedWorld;

import junit.framework.TestCase;

public class BayesianAgentTest extends TestCase {

    public void assertEquals(double d1, double d2) {
    	// Need some margin of error for floating point arithmetic.
        if (Math.abs(d1 - d2) > 0.001) {
        	super.assertEquals(d1, d2); // So the regular error is generated.
        }
    }
    
	public void testFrontierModel() {
		ObservedWorld ow = new ObservedWorld(4);
		ow.at(0, 0).observed = true;
		ow.at(1, 0).observed = true;
		ow.at(1, 0).breeze   = true;
		ow.at(0, 1).observed = true;
		ow.at(0, 1).breeze   = true;
		
		List<FrontierModel> models = FrontierModel.generateFrontierModels(ow.frontier());
		assertEquals(8, models.size());
		
		FrontierModel first = models.get(0);
		FrontierModel last  = models.get(models.size() - 1);
		
		assertEquals(.008, first.calcProb()); // first is true, true, true.
		assertEquals(.512, last.calcProb());  // last is false, false, false.
		
		assertTrue(first.isConsistent(ow));
		assertFalse(last.isConsistent(ow));
		
		assertEquals(0.310, FrontierModel.calcProb(ow, new GridPos(0,2)));
		assertEquals(0.310, FrontierModel.calcProb(ow, new GridPos(2,0)));
		assertEquals(0.862, FrontierModel.calcProb(ow, new GridPos(1,1)));
	}
}
