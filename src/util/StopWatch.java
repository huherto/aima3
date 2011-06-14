package util;

public class StopWatch {
    
    private long startTime = 0;
    
    private long sum = 0;
    private long count = 0;
    
    public void start() {
        startTime = System.currentTimeMillis();
    }
    
    public void stop() {
        if (startTime == 0)
            throw new IllegalStateException();
        
        count++;
        sum += (System.currentTimeMillis() - startTime);
        startTime = 0;
    }

}
