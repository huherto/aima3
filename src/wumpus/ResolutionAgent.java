package wumpus;

import logical.ResolutionKB;
import logical.Sentence;

public class ResolutionAgent extends LogicalAgent {

    ResolutionKB kb;
    
    public ResolutionAgent(WumpusWorld ww) {
        super(ww);
    }

	@Override
    public void tell(Sentence sentence) {
    	kb.tell(sentence);
    }
    
	@Override
    public boolean query(Sentence alpha) {
    	return kb.query(alpha);
    }
    
	@Override
	public void initkb() {
		kb = new ResolutionKB();		
	}

}
