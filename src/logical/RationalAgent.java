package logical;

import java.util.Collections;
import java.util.List;

public class RationalAgent extends Agent {

    
    ObservedWorld oWorld;
    
    public RationalAgent(WumpusWorld ww) {
        super(ww);
        oWorld = new ObservedWorld(ww.size);
    }

	@Override
    public Action nextAction(Percept percept) {
        // Update the observed world.
        ObservedWorld.Square current = oWorld.at(pos);        
        current.observed = true;
        current.breeze = percept.breeze;
                
        List<GridPos> observedNeighbors = oWorld.observed(neighbors(pos));        
    	Collections.shuffle(observedNeighbors);
        
        // Which is the closest safe square in the frontier?
        List<GridPos> safe = oWorld.safe(oWorld.frontier());
        GridPos closest = closest(pos, safe);
        if (closest != null) {
        	
        	if (estimateDistance(closest, pos) == 1) {
            	System.out.println("Neighbor is safe");
            	return getMoveDir(closest);        		
        	}
        	
        	// Move to the neighbor that gets me closer.
            GridPos closestNeighbor = closest(closest, observedNeighbors);
            if (closestNeighbor != null) {
            	System.out.println("Get closer to a safe square");
            	return getMoveDir(closestNeighbor);
            }
        }
        
        GridPos minRiskPos = minRisk(oWorld.frontier());
    	if (estimateDistance(minRiskPos, pos) == 1) {
        	System.out.println("Neighbor is minRisk");
        	return getMoveDir(minRiskPos);        		
    	}
    	
    	// Move to the neighbor that gets me closer.
        GridPos closestNeighbor = closest(minRiskPos, observedNeighbors);
        if (closestNeighbor != null) {
        	System.out.println("Get closer to a minRisk square");
        	return getMoveDir(closestNeighbor);
        }
        
        if (observedNeighbors.size() > 0) {
        	System.out.println("Coward move");
        	return getMoveDir(observedNeighbors.get(0));
        }
        
        minRiskPos = minRisk(neighbors(pos));
    	System.out.println("Kamikaze move");
        return getMoveDir(minRiskPos);
    }
	
    private int estimateDistance(GridPos pos, GridPos gpos) {
		return Math.abs(pos.x - gpos.x) + Math.abs(pos.y - gpos.y);
	}
    
    // Choose the element in the list closest to pos.
	private GridPos closest(GridPos pos, List<GridPos> list) {
        int minDistance = 2 * oWorld.size;
        GridPos closest = null;
        for(GridPos gpos: list) {
    		int distance = estimateDistance(pos, gpos);
    		if (closest == null || distance < minDistance) {
    			minDistance = distance;
    			closest = gpos;
    		}
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
    private double risk(GridPos gpos) {
        double risk = 0;
        List<GridPos> nbors = neighbors(gpos);
        for(GridPos npos : nbors ) {
            if (oWorld.at(npos).observed) {
                if (oWorld.at(npos).breeze)
                    risk += 1 / neighbors(npos).size();
                else 
                    return 0;
            }      
            else {
                risk += .20 / nbors.size();
            }
        }
        return risk;
    }

}
