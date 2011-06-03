package uncertainty;

import java.util.List;

import wumpus.ObservedWorld;

import junit.framework.TestCase;

public class BayesianAgentExample extends TestCase {

    public void assertEquals(double d1, double d2) {
        assertTrue(Math.abs(d1 - d2) <= 0.001); 
    }

    public void testFrontierModel() {
        
        ObservedWorld ow = new ObservedWorld(4);
        ow.at(0, 0).observed = true;
        ow.at(0, 1).observed = true;
        ow.at(1, 0).observed = true;
        ow.at(0, 1).breeze = true;
        ow.at(1, 0).breeze = true;
        
        List<FrontierModel> models = FrontierModel.generateFrontierModels(ow.frontier());
        assertEquals(8, models.size());
        
        // Verify theorem 13.1 (AIMA)
        double sum = 0;
        for(FrontierModel model : models) {
            double p = model.calcProb();
            assertTrue(p >= 0); 
            assertTrue(p <= 1);
            sum += p;
        }
        assertEquals(1, sum);
    }

}
