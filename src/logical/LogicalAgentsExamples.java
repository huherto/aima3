package logical;


import java.util.Random;

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
        run(200, 4, new AgentFactory() {

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
        run(200, 4, new AgentFactory() {

            @Override
            public Agent create(WumpusWorld ww) {
                return new ForwardChainAgent(ww);
            }
            
        });
    }
    
    public void testImprovedAgent() {
        run(500, 4, new AgentFactory() {

            @Override
            public Agent create(WumpusWorld ww) {
                return new RationalAgent(ww);
            }
            
        });
    }
    
}
