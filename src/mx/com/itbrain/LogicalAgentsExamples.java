package mx.com.itbrain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import mx.com.itbrain.LogicalAgentsExamples.KB.Sentence;

public class LogicalAgentsExamples extends TestCase {
    
    static Random rand = new Random();
    
    static class WumpusWorld {
        
        WumpusSquare[][] map;
        
        Agent agent;
        
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
        
        public void run(Agent agent) {
            agent.pos = new GridPos(1, 1);
            while(true) {
                Percept percept = perceptAt(agent.pos);
                Action action   = agent.nextAction(percept);
                doAction(action, agent);
            }
        }

        private void doAction(Action action, Agent agent) {
            if (action == Action.Up) {
                if (agent.pos.y + 1 < map.length)
                    agent.pos.y++;
            }
            else if (action == Action.Down) {
                if (agent.pos.y > 0)
                    agent.pos.y--;
            }
            else if (action == Action.Right) {
                if (agent.pos.x + 1 < map.length)
                    agent.pos.x++;                
            }
            else if (action == Action.Left) {
                if (agent.pos.x - 1 > 0)
                    agent.pos.x--;                
            }
        }

        private Percept perceptAt(GridPos agentPos) {
            List<GridPos> list = neighbors(agentPos);
            Percept p = new Percept();
            for(GridPos pos: list ) {
                WumpusSquare square = squareAt(pos);
                if (square.hasPit)
                    p.breeze = true;
                if (square.hasWumpus)
                    p.stench = true;
            }
            WumpusSquare square = squareAt(agentPos);
            if (square.hasGold)
                p.glitter = true;
            return p;
        }
        
        private WumpusSquare squareAt(GridPos pos) {
            return map[pos.x][pos.y];
        }

        private List<GridPos> neighbors(GridPos pos) {
            List<GridPos> res = new ArrayList<GridPos>();
            if (pos.x > 0)
                res.add(new GridPos(pos.x - 1, pos.y));
            if (pos.y > 0)
                res.add(new GridPos(pos.x, pos.y - 1));
            if (pos.x + 1 < map.length)
                res.add(new GridPos(pos.x + 1, pos.y));
            if (pos.y + 1 < map[pos.x].length)
                res.add(new GridPos(pos.x, pos.y + 1));
            return res;
        }
    }
    
    static class WumpusSquare {
        boolean hasPit    = false;
        boolean hasGold   = false;
        boolean hasWumpus = false;
    };
    
    static class GridPos {
        public GridPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int x, y;
    }
    
    static class Percept {
        boolean stench  = false;
        boolean breeze  = false;
        boolean glitter = false;
        boolean bump    = false;
        boolean scream  = false;
    }
    
    enum Action {
        Up, Down, Left, Right, Grab, Shoot, Climb
    }
    
    static abstract class Agent {

        GridPos pos;
        
        public abstract Action nextAction(Percept percept);
  
    }
    
    static class KB {
        
        List<Sentence> sentences = new ArrayList<Sentence>();
        
        class Model {
            private HashMap<String, Boolean> values;
            public boolean isTrue(String name) {
                return values.get(name);
            }
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
                return model.isTrue(name);
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
                return s1.isTrue(model) && s2.isTrue(model);
            }
        }
        
        static class Or extends BinarySentence {
            public Or(Sentence s1, Sentence s2) {
                super(s1, s2);
            }

            @Override
            public boolean isTrue(Model model) {
                return s1.isTrue(model) || s2.isTrue(model);
            }
        }
        
        static class If extends BinarySentence {
            public If(Sentence s1, Sentence s2) {
                super(s1, s2);
            }

            @Override
            public boolean isTrue(Model model) {
                if (s1.isTrue(model)) {
                    return s2.isTrue(model);
                }
                return true;
            }            
        }
        
        static class Iff extends BinarySentence {
            public Iff(Sentence s1, Sentence s2) {
                super(new If(s1,s2), new If(s2, s1));
            }

            @Override
            public boolean isTrue(Model model) {
                return s1.isTrue(model) && s2.isTrue(model);
            }            
        }
        
        void tell(Sentence sentence) {
            sentences.add(sentence);
        }

        public Action makeActionQuery(int time) {
            // TODO Auto-generated method stub
            return null;
        }
    }
    
    static class LogicalAgent extends Agent {
        
        private KB kb;
        
        int time = 0;
        
        public Action nextAction(Percept percept) {
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
