package mx.com.itbrain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

public class LogicalAgentsExamples extends TestCase {
    
    static Random rand = new Random();
    
    static class WumpusWorld {
    	
    	final static int SIZE = 4;
        
        WumpusSquare[][] map;
        
        Agent agent;
        
        WumpusWorld() {
            
            map = new WumpusSquare[SIZE][SIZE];
            
            int wumpusPos = rand.nextInt(SIZE * SIZE - 1);
            int goldPos   = rand.nextInt(SIZE * SIZE);
            
            for(int i = 0; i < SIZE; i++) {
                for(int j = 0; j < SIZE; j++) {
                        
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
                        //System.out.println("Gold is in " + new GridPos(i, j));
                    }
                    goldPos--;
                    if (rand.nextInt(5) == 0) {
                        current.hasPit = true;
                    }
                }
            }
        }
        
        public int run(Agent agent) {
        	
        	int points = 0;
            agent.pos = new GridPos(0, 0);
            while(true) {
            	
            	WumpusSquare currentSquare = squareAt(agent.pos);
            	if (currentSquare.hasPit) {
            		points -= 1000;
            		System.out.println("Agent fell in the pit");
            		return points;
            	}
            	if (currentSquare.hasWumpus) {
            		points -= 1000;
            		System.out.println("Agent was eaten");
            		return points;
            	}
            	if (currentSquare.hasGold) {
            		points += 1000;
            		System.out.println("Agent found the gold");
            		return points;
            	}
            	
                Percept percept = perceptAt(agent.pos);
                Action action   = agent.nextAction(percept);
                doAction(action, agent);
                points--;
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
                if (agent.pos.x > 0)
                    agent.pos.x--;                
            }
//            System.out.println(agent.pos);
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

        public static List<GridPos> neighbors(GridPos pos) {
            List<GridPos> res = new ArrayList<GridPos>();
            if (pos.x > 0)
                res.add(new GridPos(pos.x - 1, pos.y));
            if (pos.y > 0)
                res.add(new GridPos(pos.x, pos.y - 1));
            if (pos.x + 1 < SIZE)
                res.add(new GridPos(pos.x + 1, pos.y));
            if (pos.y + 1 < SIZE)
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

		@Override
		public String toString() {
			return x+"."+y;
		}
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
    
    static class StupidAgent extends Agent {
    	    	
		@Override
		public Action nextAction(Percept percept) {
			int idx = rand.nextInt(4);
			Action[] values = Action.values();
			return values[idx];
		}
    	
    }
    
    public void testStupidAgent() {
    	int totalPoints = 0;
    	for(int i = 0; i < 1000; i++) {
    		WumpusWorld ww = new WumpusWorld();
    		totalPoints += ww.run(new StupidAgent());
    	}	
    	System.out.println("totalPoints="+totalPoints);
    }
    
    static class KnowledgeBase {
        
        Set<Sentence> sentences = new HashSet<Sentence>();
        
        void tell(Sentence sentence) {
            sentences.add(sentence);
        }

        public boolean query(String n) {
        	return query(new Symbol(n));
        }
        
        public boolean query(Sentence alpha) {
        	
        	Set<Symbol> symbols = new HashSet<Symbol>();
        	for(Sentence sentence : sentences) {
        		symbols.addAll(sentence.symbols());
        	}
        	symbols.addAll(alpha.symbols());
        	
        	return ttCheckAll(alpha, new LinkedList<Symbol>(symbols), new Model());
        }

		private boolean ttCheckAll(Sentence alpha, List<Symbol> symbols, Model model) {
			
			if (symbols.isEmpty()) {
				if (isTrue(model))
					return alpha.isTrue(model);
				else
					return true;
			}
			else {
				Symbol P = symbols.get(0);
				List<Symbol> rest = symbols.subList(1, symbols.size());
				
				boolean result = false;
				model.set(P, true);								
				if (ttCheckAll(alpha, rest, model)) {
					model.set(P, false);
					if (ttCheckAll(alpha, rest, model)) {
						result = true;
					}
				}
				model.unset(P);
				return result;
			}
		}

		private boolean isTrue(Model model) {
			for(Sentence s : sentences) {
				if (!s.isTrue(model))
					return false;
			}
			return true;
		}
        
    }
    
    static class Model {
        private Map<String, Boolean> values = new HashMap<String, Boolean>();
        
        public Model() {
        }
        
        public void unset(Symbol p) {
        	values.remove(p.name);
		}

		public void set(Symbol p, boolean value) {
			values.put(p.name, value);
		}
		
		public boolean isTrue(String name) {
            return values.get(name);
        }
    }

    static abstract class Sentence {
        abstract boolean isTrue(Model model);
		abstract Set<Symbol> symbols();
		
		@Override
		public boolean equals(Object other) {
			if (!(other instanceof Sentence))
				return false;
			if (this == other)
				return true;
			
			// Convert to a cannonical representation to be able to compare.
			String s1 = toString();
			String s2 = other.toString();
			return s1.equals(s2);
		}
		
		@Override
		public int hashCode() {
			return toString().hashCode();			
		}
    }
    
    static class True extends Sentence {
        public boolean isTrue(Model model) {
            return true;
        }

		@Override
		public Set<Symbol> symbols() {
			return new HashSet<Symbol>();
		}
        
		public String toString() {
			return "True";
		}
		
    }
    
    static class False extends Sentence {
        public boolean isTrue(Model model) {
            return false;
        }
        
		@Override
		public Set<Symbol> symbols() {
			return new HashSet<Symbol>();
		}
		
		public String toString() {
			return "False";
		}
    }
    
    static class Symbol extends Sentence {
        public String name;

        public Symbol(String name) {
			this.name = name;
		}

		@Override
        public boolean isTrue(Model model) {
            return model.isTrue(name);
        }

		@Override
		public Set<Symbol> symbols() {
			Set<Symbol> res = new HashSet<Symbol>();
			res.add(this);
			return res;
		}
		
		public String toString() {
			return name;
		}

    }
    
    static class Not extends Sentence {
        private Sentence s;
        public Not(Sentence s) {
            this.s = s;
        }
        public Not(String name) {
            this.s = new Symbol(name);
        }
        
        @Override
        public boolean isTrue(Model model) {
            return !s.isTrue(model);
        }            
        
		@Override
		public Set<Symbol> symbols() {
			Set<Symbol> res = new HashSet<Symbol>();
			res.addAll(s.symbols());
			return res;
		}
		
		public String toString() {
			return "(¬"+s+")";
		}
    }
    
    static abstract class BinarySentence extends Sentence {
        protected Sentence s1;
        protected Sentence s2;
        public BinarySentence(Sentence s1, Sentence s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
        
		@Override
		public Set<Symbol> symbols() {
			Set<Symbol> res = new HashSet<Symbol>();
			res.addAll(s1.symbols());
			res.addAll(s2.symbols());
			return res;
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
        
		public String toString() {
			return s1+"=>"+s2;
		}
    }
    
    static class Iff extends BinarySentence {
        public Iff(Sentence s1, Sentence s2) {
            super(s1, s2);
        }

        @Override
        public boolean isTrue(Model model) {
        	return s1.isTrue(model) == s2.isTrue(model);
        }            
        
		public String toString() {
			return s1+"<=>"+s2;
		}
    }
    
    static class And extends Sentence {
    	List<Sentence> sList = new ArrayList<Sentence>();
        public And(Sentence... sentences) {
        	for(Sentence s: sentences) {
        		sList.add(s);
        	}
        }
        
        public And(String... names) {
        	for(String n: names) {
        		sList.add(new Symbol(n));
        	}
        }

        @Override
        public boolean isTrue(Model model) {
        	for(Sentence s : sList) {
        		if (!s.isTrue(model))
        			return false;
        	}
        	return true;
        }
        
		@Override
		public Set<Symbol> symbols() {
			Set<Symbol> res = new HashSet<Symbol>();
			for(Sentence s : sList) {
				res.addAll(s.symbols());
			}
			return res;
		}
		
		public String toString() {
			StringBuilder str = new StringBuilder();
			for(Sentence s : sList) {
				if (str.length() > 0)
					str.append(" and ");
				str.append(s.toString());
			}
			
			return "(" + str + ")";
		}
    }
    
    static class Or extends Sentence {
    	List<Sentence> sList = new ArrayList<Sentence>();
        public Or(Sentence... sentences) {
        	for(Sentence s: sentences) {
        		sList.add(s);
        	}
        }
        
        public Or(String... names) {
        	for(String n: names) {
        		sList.add(new Symbol(n));
        	}
        }

        @Override
        public boolean isTrue(Model model) {
        	for(Sentence s : sList) {
        		if (s.isTrue(model))
        			return true;
        	}
        	return false;
        }
        
		@Override
		public Set<Symbol> symbols() {
			Set<Symbol> res = new HashSet<Symbol>();
			for(Sentence s : sList) {
				res.addAll(s.symbols());
			}
			return res;
		}
		
		public String toString() {
			StringBuilder str = new StringBuilder();
			for(Sentence s : sList) {
				if (str.length() > 0)
					str.append(" or ");
				str.append(s.toString());
			}
			
			return "(" + str + ")";
		}
    }
    
    static class LogicalAgent extends Agent {
        
        private KnowledgeBase kb;
        
        public LogicalAgent() {
        	kb = new KnowledgeBase();
        	
        	kb.tell(new Not("P.0.0"));
        	
            for(int x = 0; x < WumpusWorld.SIZE; x++) {
                for(int y = 0; y < WumpusWorld.SIZE; y++) {
                	GridPos pos = new GridPos(x, y);
                	List<GridPos> neighbors = WumpusWorld.neighbors( pos );
                	
                	String[] list = new String[neighbors.size()];
                	for(int i = 0; i < list.length; i++) {
                		list[i] = "P."+neighbors.get(i);
                	}
                	
                	kb.tell(new Iff(new Symbol("B."+pos), new Or(list)));
                }
            }
        }
        
        public Action nextAction(Percept percept) {
            makePerceptSentences(percept);
            
            Action action = makeActionQuery();
            
            System.out.println("going "+action);
            
            return action;
        }

        private Action makeActionQuery() {
        	List<GridPos> neighbors = WumpusWorld.neighbors(pos);
        	for(GridPos npos : neighbors) {
        		if (kb.query(new Not("P."+npos))) {
        			return move(npos);
        		}
        	}
        	return Action.Up;
		}

		private Action move(GridPos dest) {
			if (pos.x == dest.x) {
				if (pos.y + 1 == dest.y)
					return Action.Up;
				if (pos.y - 1 == dest.y)
					return Action.Down;
				throw new IllegalStateException();
			}
			if (pos.y == dest.y) {
				if (pos.x + 1 == dest.x)
					return Action.Right;
				if (pos.x - 1 == dest.x)
					return Action.Left;
				throw new IllegalStateException();
			}
			throw new IllegalStateException();
		}

		private void makePerceptSentences(Percept percept) {
			if (percept.breeze) {
				kb.tell(new Symbol("B."+pos));
			}
			else {
				kb.tell(new Not("B."+pos));				
			}
        }

    }
    
    public void testLogicalAgent() {
    	int totalPoints = 0;
    	for(int i = 0; i < 1; i++) {
    		WumpusWorld ww = new WumpusWorld();
    		totalPoints += ww.run(new LogicalAgent());
    	}	
    	System.out.println("totalPoints="+totalPoints);
    }
}
