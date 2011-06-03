package wumpus;

import java.util.Collections;
import java.util.List;

import logical.Iff;
import logical.Not;
import logical.Or;
import logical.Sentence;
import logical.Symbol;


public abstract class LogicalAgent extends Agent {

	public LogicalAgent(WumpusWorld ww) {
		super(ww);
		
		initkb();
		
    	tell(new Not("P.0.0"));
    	
        for(int x = 0; x < getWorldSize(); x++) {
            for(int y = 0; y < getWorldSize(); y++) {
            	GridPos pos = new GridPos(x, y);
            	List<GridPos> neighbors = neighbors( pos );
            	
            	String[] list = new String[neighbors.size()];
            	for(int i = 0; i < list.length; i++) {
            		list[i] = "P."+neighbors.get(i);
            	}
            	
            	tell(new Iff(new Symbol("B."+pos), new Or(list)));
            }
        }
	}
	
	public abstract void initkb();
	
    public abstract void tell(Sentence sentence);
    
    public abstract boolean query(Sentence alpha);

	public Action nextAction(Percept percept) {
	    makePerceptSentences(percept);
	    
	    Action action = makeActionQuery();
	    
	    
	    return action;
	}

	private Action makeActionQuery() {
		List<GridPos> neighbors = neighbors(pos);
		Collections.shuffle(neighbors);
		for(GridPos npos : neighbors) {
			if (query(new Not("P."+npos))) {
			    tell(new Not("P."+npos));
	            System.out.println("Safe move");
	            return getMoveDir(npos);
			}
		}
	    for(GridPos npos : neighbors) {
	        if (query(new Symbol("P."+npos))) {
	            tell(new Symbol("P."+npos));
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
		if (percept.breeze) {
			tell(new Symbol("B."+pos));
		}
		else {
			tell(new Not("B."+pos));				
		}
	}

}