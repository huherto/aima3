package mx.com.itbrain;

import java.util.HashMap;
import java.util.Random;

import junit.framework.TestCase;
import mx.com.itbrain.LogicalAgentsExamples.KB.Model;
import mx.com.itbrain.LogicalAgentsExamples.KB.Sentence;

public class LogicalAgentsExamples extends TestCase {
    
    static Random rand = new Random();
    
    static class WumpusWorld {
        
        class WumpusSquare {
            boolean hasPit = false;
            boolean hasGold = false;
            boolean hasWumpus = false;
        };
        
        WumpusSquare[][] map;
        
        WumpusWorld() {
            
            map = new WumpusSquare[4][4];
            
            int wumpusPos = rand.nextInt() % 15;
            int goldPos   = rand.nextInt() % 16;
            
            for(int i = 0; i < map.length; i++) {
                for(int j = 0; j < map[i].length; j++) {
                        
                    map[i][j] = new WumpusSquare();
                    WumpusSquare current = map[i][j];
                    if (i > 0 && j> 0) {
                        if (wumpusPos == 0) {
                            current.hasWumpus = true;
                        }
                        wumpusPos--;
                    }
                    if (goldPos == 0) {
                        current.hasGold = true;
                    }
                    goldPos--;
                    if (rand.nextInt() % 5 == 0) {
                        current.hasPit = true;
                    }
                }
            }
        }
    }
    
    static class KB {
        
        class Model {
            HashMap<String, Boolean> values;
        }
    
        interface Sentence {
            boolean isTrue(Model model);
        }
        
        interface AtomicSentence extends Sentence {
        }
        
        static class True implements AtomicSentence {
            public boolean isTrue(Model model) {
                return true;
            }
        }
        
        static class False implements AtomicSentence {
            public boolean isTrue(Model model) {
                return false;
            }
        }
        
        static class Symbol implements AtomicSentence {
            public String name;

            @Override
            public boolean isTrue(Model model) {
                // TODO Auto-generated method stub
                return false;
            }
        }
        
        static class Not implements Sentence {
            private Sentence s;
            public Not(Sentence s) {
                this.s = s;
            }
            
            @Override
            public boolean isTrue(Model model) {
                return !s.isTrue(null);
            }
            
        }
        
        static abstract class BinarySentence implements Sentence {
            protected Sentence s1;
            protected Sentence s2;
            public BinarySentence(Sentence s1, Sentence s2) {
                this.s1 = s2;
                this.s2 = s2;
            }
        }
        
        static class And extends BinarySentence {
            public And(Sentence s1, Sentence s2) {
                super(s1, s2);
            }

            @Override
            public boolean isTrue(Model model) {
                // TODO Auto-generated method stub
                return false;
            }
        }
        
        static class Or extends BinarySentence {
            public Or(Sentence s1, Sentence s2) {
                super(s1, s2);
            }

            @Override
            public boolean isTrue(Model model) {
                // TODO Auto-generated method stub
                return false;
            }
        }
        
        static class If extends BinarySentence {
            public If(Sentence s1, Sentence s2) {
                super(s1, s2);
            }

            @Override
            public boolean isTrue(Model model) {
                // TODO Auto-generated method stub
                return false;
            }            
        }
        
        static class Iff extends BinarySentence {
            public Iff(Sentence s1, Sentence s2) {
                super(s1, s2);
            }

            @Override
            public boolean isTrue(Model model) {
                // TODO Auto-generated method stub
                return false;
            }            
        }
        
        void tell(Sentence sentence) {
            
        }

        public Action makeActionQuery(int time) {
            // TODO Auto-generated method stub
            return null;
        }
    }
    
    class Percept {
        
    }
    
    class Action {
        
    }
    
    class Agent {
        
        KB kb;
        
        int time = 0;
        
        Action nextAction(Percept percept) {
            kb.tell(makePerceptSentence(percept, time));
            Action action = kb.makeActionQuery(time);
            kb.tell(makeActionSentence(action, time));
            time++;
            return action;
        }

        private Sentence makeActionSentence(Action action, int time) {
            return null;
        }

        private Sentence makePerceptSentence(Percept percept, int time) {
            return null;
        }
    }

}
