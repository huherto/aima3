/**
 * 
 */
package logical;

import java.util.Collections;
import java.util.List;


class TruthTableAgent extends Agent {
    
    private TruthTableKB kb;
    
    public TruthTableAgent(WumpusWorld ww) {
        super(ww);
    	kb = new TruthTableKB();
    	
    	kb.tell(new Not("P.0.0"));
    	
        for(int x = 0; x < getWorldSize(); x++) {
            for(int y = 0; y < getWorldSize(); y++) {
            	GridPos pos = new GridPos(x, y);
            	List<GridPos> neighbors = neighbors( pos );
            	
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
        
        
        return action;
    }

    private Action makeActionQuery() {
    	List<GridPos> neighbors = neighbors(pos);
    	Collections.shuffle(neighbors);
    	for(GridPos npos : neighbors) {
    		if (kb.query(new Not("P."+npos))) {
    		    kb.tell(new Not("P."+npos));
                System.out.println("Safe move");
                return getMoveDir(npos);
    		}
    	}
        for(GridPos npos : neighbors) {
            if (kb.query("P."+npos)) {
                kb.tell(new Symbol("P."+npos));
            }
            else {
                System.out.println("Risky move");
                return getMoveDir(npos);
            }
        }
        System.out.println("Kamikase move");
    	return getMoveDir(neighbors.get(0));
	}

	private void makePerceptSentences(Percept percept) {
		if (percept.breeze || percept.stench) {
			kb.tell(new Symbol("B."+pos));
		}
		else {
			kb.tell(new Not("B."+pos));				
		}
    }

}