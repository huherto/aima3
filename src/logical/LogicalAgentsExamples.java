package logical;


import java.util.Random;

import uncertainty.BayesianAgent;

import junit.framework.TestCase;

public class LogicalAgentsExamples extends TestCase {
    
    static Random rand = new Random();
    
    interface AgentFactory {
        Agent create(WumpusWorld ww);
    }
    
    public void run(int MAX, int size, AgentFactory fact) {
        int values[] = new int[MAX];
        int sum = 0;
        for(int i = 0; i < MAX; i++) {
            System.out.println("********* new game *********");
            WumpusWorld ww = new WumpusWorld(size);
            Agent agent = fact.create(ww);
            values[i] = ww.run(agent);
            sum += values[i];
        } 
        int mean = sum / MAX;
        
        sum = 0;
        for(int i = 0; i < MAX; i++) {
            int diff = values[i] - mean;
            int square = diff * diff;
            sum += square;
        }
        
        int stddev = (int)Math.sqrt(sum/MAX);
        System.out.println("mean="+ mean);
        System.out.println("std dev="+ stddev);
        System.out.println(""+(mean - 2*stddev)+"..."+(mean + 2*stddev));
    }
    
    public void testStupidAgent() {
        run(10000, 4, new AgentFactory() {

            @Override
            public Agent create(WumpusWorld ww) {
                return new StupidAgent(ww);
            }
            
        });
    }
    
    public void testTTAgent() {
        run(100, 3, new AgentFactory() {

            @Override
            public Agent create(WumpusWorld ww) {
                return new TruthTableAgent(ww);
            }
            
        });
    }
    
    public void testFCAgent() {
        run(10000, 4, new AgentFactory() {

            @Override
            public Agent create(WumpusWorld ww) {
                return new ForwardChainAgent(ww);
            }
            
        });
    }
    
    public void testSimpleRiskAwareAgent() {    	
        	
        run(10000, 4, new AgentFactory() {

            @Override
            public Agent create(WumpusWorld ww) {
                return new SimpleRiskAwareAgent(ww);
            }
            
        });
    }
    
    public void testBayesianAgent() {    	
    	
        run(10000, 4, new AgentFactory() {

            @Override
            public Agent create(WumpusWorld ww) {
                return new BayesianAgent(ww);
            }
            
        });
    }
    
    public void testObservedWorld() {
    	ObservedWorld ow = new ObservedWorld(4);
    	assertEquals(16, ow.allSquares().size());
    	assertEquals(0, ow.observed(ow.allSquares()).size());
    	assertEquals(0, ow.frontier().size());
    	
    	ow.at(0, 0).observed = true;
    	assertEquals(1, ow.observed(ow.allSquares()).size());
    	assertEquals(2, ow.frontier().size());
    	
    	assertTrue(ow.isSafe(new GridPos(0,1)));
    	ow.at(0, 0).breeze = true;
    	assertFalse(ow.isSafe(new GridPos(0,1)));
    	
    	assertEquals(3, ow.neighbors(new GridPos(0, 1)).size());
    	ow.at(0, 1).observed = true;
    	assertEquals(3, ow.frontier().size());
    }
    
}
