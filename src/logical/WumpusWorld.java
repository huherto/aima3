/**
 * 
 */
package logical;

import java.util.ArrayList;
import java.util.List;

public class WumpusWorld {
    	
    	int size;
        
        WumpusSquare[][] map;
        
        Agent agent;
        
        WumpusWorld(int gridSize) {
            this.size = gridSize;
            
            map = new WumpusSquare[size][size];
            
            int wumpusPos = LogicalAgentsExamples.rand.nextInt(size * size - 1);
            int goldPos   = LogicalAgentsExamples.rand.nextInt(size * size);
            
            for(int i = 0; i < size; i++) {
                for(int j = 0; j < size; j++) {
                        
                    map[i][j] = new WumpusSquare();
                    WumpusSquare current = map[i][j];
                    if (i > 0 && j> 0) {
                        if (wumpusPos == 0) {
                            // current.hasWumpus = true;
                        }
                        wumpusPos--;
                    }
                    if (goldPos == 0) {
                        current.hasGold = true;
                        System.out.println("Gold is in " + new GridPos(i, j));
                    }
                    goldPos--;
                    if (LogicalAgentsExamples.rand.nextInt(5) == 0) {
                        current.hasPit = true;
                    }
                }
            }
        }
        
        public int run(Agent agent) {
        	
        	int points = 0;
            agent.pos = new GridPos(0, 0);
            for(int i = 0; i < 100; i++) {
            	
            	WumpusSquare currentSquare = squareAt(agent.pos);
            	if (currentSquare.hasPit) {
            		points -= 1000;
            		System.out.println("Agent fell in the pit");
            		//throw new RuntimeException("Agent fell in the pit");
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
                System.out.println("going "+action+" "+agent.pos);
                points--;
            }
            System.out.println("Chances are over");            
            return points;
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

        public List<GridPos> neighbors(GridPos pos) {
            List<GridPos> res = new ArrayList<GridPos>();
            if (pos.x > 0)
                res.add(new GridPos(pos.x - 1, pos.y));
            if (pos.y > 0)
                res.add(new GridPos(pos.x, pos.y - 1));
            if (pos.x + 1 < size)
                res.add(new GridPos(pos.x + 1, pos.y));
            if (pos.y + 1 < size)
                res.add(new GridPos(pos.x, pos.y + 1));
            return res;
        }
    }