/**
 * 
 */
package wumpus;

import logical.Sentence;
import logical.TruthTableKB;

class TruthTableAgent extends LogicalAgent {
    
    TruthTableKB kb;
    
    public void tell(Sentence sentence) {
    	kb.tell(sentence);
    }
    
    public boolean query(Sentence alpha) {
    	return kb.query(alpha);
    }
    
    public TruthTableAgent(WumpusWorld ww) {
        super(ww);
    }

	@Override
	public void initkb() {
		kb = new TruthTableKB();		
	}

}