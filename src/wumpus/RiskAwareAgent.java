package wumpus;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import wumpus.ObservedWorld.Square;



public abstract class RiskAwareAgent extends Agent {

    protected ObservedWorld oWorld;
    
    public RiskAwareAgent(WumpusWorld ww) {
        super(ww);
        oWorld = new ObservedWorld(ww.size);
    }

	@Override
    public Action nextAction(Percept percept) {
        // Update the observed world.
        ObservedWorld.Square current = oWorld.at(pos);        
        current.observed = true;
        current.breeze = percept.breeze;
                
//        print(System.out);
        
        List<GridPos> frontier = oWorld.frontier();
        GridPos minRiskPos = minRisk(frontier);
        return getCloserTo(minRiskPos);
    }
	
    private Action getCloserTo(GridPos targetPos) {

    	if (estimateDistance(targetPos, pos) == 1) {
           	return getMoveDir(targetPos);    		
    	}
    	
    	List<GridPos> observedNeighbors = oWorld.observed(neighbors(pos));        
    	
    	// Move to the neighbor that gets me closer.
        GridPos closestNeighbor = closest(targetPos, observedNeighbors);
       	System.out.println("Get closer to square "+targetPos);
       	return getMoveDir(closestNeighbor);
	}

	private int estimateDistance(GridPos pos, GridPos gpos) {
		return Math.abs(pos.x - gpos.x) + Math.abs(pos.y - gpos.y);
	}
    
    // Find the element in the list closest to pos.
	private GridPos closest(GridPos targetPos, List<GridPos> list) {
		
		if (list.isEmpty()) {
			throw new RuntimeException("list shouldn't be empty");
		}
		
    	Collections.shuffle(list);
    	
        int minDistance = 2 * oWorld.size;
        GridPos closest = null;
        for(GridPos gpos: list) {
    		int distance = estimateDistance(targetPos, gpos);
    		if (closest == null || distance < minDistance) {
    			minDistance = distance;
    			closest = gpos;
    		}
        }
        if (closest == null) {
        	throw new RuntimeException("Cannot find element");
        }
        return closest;
	}

    // Choose the element in the list with the minimum risk.
	private GridPos minRisk(List<GridPos> list) {
        double minRisk = 1;
        GridPos minRiskPos = null;
        for(GridPos gpos: list) {
    		double risk = risk(gpos);
    		if (minRiskPos == null || risk < minRisk) {
    			minRisk = risk;
    			minRiskPos = gpos;
    		}
        }
        return minRiskPos;
	}
	
	// Calculate the risk of this square.
    public abstract double risk(GridPos gpos);
    
    public void print(PrintStream out) {
    	List<GridPos> frontier = oWorld.frontier();
    	for(int y = oWorld.size - 1; y >= 0; y--) {
    		out.printf("%2d ", y);
        	for(int x = 0; x < oWorld.size; x++) {
        		Square square = oWorld.at(x, y);
        		if (square.observed) {
        			if (square.breeze) {
        				out.print(" B ");
        			}
        			else {
        				 out.print("   ");
        			}
        		}
        		else if (frontier.contains(new GridPos(x, y))) {
        			out.print(" F ");
        		}
        		else {
        			out.print(" X ");
        		}
        	}    		
        	out.println();
    	}
    }
}
