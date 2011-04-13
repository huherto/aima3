package neuralnets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

public class Hopfield extends TestCase {
    
    static class Connection {
        int first;
        int second;
        int weight;
        Connection(int first, int second, int weight) {
            this.first = first;
            this.second = second;
            this.weight = weight;
        }
    };
    
    static class Net {
        List<Connection> connections = new ArrayList<Connection>();
        boolean units[];
        Random rand = new Random();
        
        Net(int size) {
            units = new boolean[size];
        }
        
        void add(int first, int second, int weight) {
            connections.add(new Connection(first, second, weight));
        }
        
        static Net createNetwork() {
            Net net = new Net(7);
            net.add(0, 1, -1);
            net.add(0, 2, +1);
            net.add(0, 3, -1);
            net.add(1, 3, +3);
            net.add(2, 3, -1);
            net.add(2, 4, +2);
            net.add(2, 5, +1);
            net.add(3, 5, -2);
            net.add(3, 6, +3);
            net.add(4, 5, +1);
            net.add(5, 6, -1);
            return net;
        }
        
        void randomize() {
            for(int i = 0; i < units.length; i++) {
                units[i] = rand.nextBoolean(); 
            }
        }    
        
        void searchStableState() {
            
            int noChange = 0;
            while(noChange < units.length * 10) {
                int idx = rand.nextInt(units.length);
                int sum = sumConnections(idx);
                boolean old = units[idx];
                units[idx] = (sum > 0);
                if (units[idx] == old) {
                    noChange++;
                }
                else {
                    noChange = 0;
                }
            }
        }

        private int sumConnections(int idx) {
            int sum = 0;
            for(Connection c:connections) {
                if (c.first == idx) {
                    if (units[c.second]) {
                        sum += c.weight;
                    }
                }
                else if (c.second == idx) {
                    if (units[c.first]) {
                        sum += c.weight;
                    }
                }
            }
            return sum;
        }
        
        public String toString() {
            StringBuilder buf = new StringBuilder();
            for(int i = 0; i < units.length; i++) {
                buf.append(units[i]?"1":"0");
            }
            return buf.toString();
        }
    }
    
    class Counter {
        int value = 0;
    };
    
    public void testBasic() {
        
        Map<String, Counter> map = new HashMap<String, Counter>();
        Net net = Net.createNetwork();
        for(int i = 0; i < 10000; i++) {
            net.randomize();
            net.searchStableState();
            String key = net.toString();
            if (!map.containsKey(key)) {
                map.put(key, new Counter());
            }
            map.get(key).value++;
        }
        
        for(String key : map.keySet()) {
            System.out.println(key + " " + map.get(key).value);
        }
    }
    
}
