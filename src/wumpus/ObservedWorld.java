/**
 * 
 */
package wumpus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ObservedWorld {
	
    public class Square {
        public boolean observed = false;
        public boolean breeze = false;
    };

	Square map[][];
    
    int size;
    
    public ObservedWorld(int size) {
		this.size = size;
        map = new Square[size][size];
                    
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                map[i][j] = new Square();
            }
        }
    }

    public Square at(GridPos pos) {
        return map[pos.x][pos.y];
    }
    
    public Square at(int x, int y) {
        return map[x][y];
    }
    
    public List<GridPos> allSquares() {
    	List<GridPos> res = new LinkedList<GridPos>();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
            	GridPos gpos = new GridPos(i,j);
           		res.add(gpos);                		
            }
        }
		return res;
	}
    
    public List<GridPos> frontier() {
    	List<GridPos> res = new LinkedList<GridPos>();
    	List<GridPos> observed = observed(allSquares());
    	for(GridPos gpos: observed) {
    		for(GridPos npos : neighbors(gpos)) {
    			if (!at(npos).observed) {
    				if (!res.contains(npos)) {
    					res.add(npos);
    				}
    			}
    		}
    	}
    	return res;
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

	public List<GridPos> safe(List<GridPos> list) {
    	List<GridPos> res = new LinkedList<GridPos>();
    	for(GridPos gpos: list) {
        	if (isSafe(gpos)) {
				res.add(gpos);
    		}
    	}
    	return res;
    }
    
    public List<GridPos> observed(List<GridPos> list) {
    	List<GridPos> res = new LinkedList<GridPos>();
    	for(GridPos gpos: list) {
        	if (at(gpos).observed) {
				res.add(gpos);
    		}
    	}
    	return res;
    }
    
    // A square is safe if one of its neighbors has been observed and 
    // it doesn't have a breeze.
    public boolean isSafe(GridPos gpos) {
        List<GridPos> nbors = neighbors(gpos);
        for(GridPos npos : nbors ) {
            if (at(npos).observed && !at(npos).breeze)
                return true;
        }
        return false;
    }
    
}