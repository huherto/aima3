package logical;


import java.util.Random;

import junit.framework.TestCase;

public class LogicalAgentsExamples extends TestCase {
    
    static Random rand = new Random();
    
    
    public void run(int MAX, Agent agent) {
        int values[] = new int[MAX];
        int sum = 0;
        for(int i = 0; i < MAX; i++) {
            WumpusWorld ww = new WumpusWorld();
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
        run(100, new StupidAgent());
    }
    
    public void testTTAgent() {
        run(100, new TruthTableAgent());
    }
    
    public void testFCAgent() {
        run(100, new ForwardChainAgent());
    }
}
