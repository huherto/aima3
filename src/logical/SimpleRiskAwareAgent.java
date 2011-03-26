package logical;

import java.util.List;


public class SimpleRiskAwareAgent extends RiskAwareAgent {

	public SimpleRiskAwareAgent(WumpusWorld ww) {
		super(ww);
	}

	// Calculate the risk of this square.
    public double risk(GridPos gpos) {
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
