package logical;

import java.util.Collections;
import java.util.List;

class ForwardChainAgent extends Agent {
    
    private ForwardChainKB kb;
    
    public ForwardChainAgent() {
        kb = new ForwardChainKB();
        
        kb.tell(HornClause.NegFact("P.0.0"));
        
        for(int x = 0; x < WumpusWorld.SIZE; x++) {
            for(int y = 0; y < WumpusWorld.SIZE; y++) {
                GridPos pos = new GridPos(x, y);
                List<GridPos> neighbors = WumpusWorld.neighbors( pos );
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
        List<GridPos> neighbors = WumpusWorld.neighbors(pos);
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
        if (percept.breeze || percept.stench) {
            kb.tell(HornClause.Fact("B."+pos));
        }
        else {
            kb.tell(HornClause.NegFact("B."+pos));             
        }
    }

}