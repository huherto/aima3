package wumpus;

import java.util.Collections;
import java.util.List;

import logical.ForwardChainKB;
import logical.HornClause;


class ForwardChainAgent extends Agent {
    
    private ForwardChainKB kb;
    
    public ForwardChainAgent(WumpusWorld ww) {
        super(ww);
        kb = new ForwardChainKB();
        
        // Panic! this is ignored in FC!
        kb.tell(HornClause.NegFact("P.0.0"));
        
        for(int x = 0; x < getWorldSize(); x++) {
            for(int y = 0; y < getWorldSize(); y++) {
                GridPos pos = new GridPos(x, y);
                List<GridPos> neighbors = neighbors( pos );
                for(GridPos npos : neighbors) {
                    kb.tell(new HornClause("B."+pos,"P."+npos));
                }
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
            if (kb.query("P."+npos)) {
                kb.tell(HornClause.Fact("P."+npos));
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
            kb.tell(HornClause.Fact("B."+pos));
        }
        else {
            // Panic! this is ignored in FC!
            kb.tell(HornClause.NegFact("B."+pos));
            List<GridPos> neighbors = neighbors(pos);
            for(GridPos npos : neighbors) {
                kb.tell(HornClause.NegFact("P"+npos));
            }
        }
    }

}