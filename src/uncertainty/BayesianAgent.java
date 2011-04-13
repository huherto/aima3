package uncertainty;

import logical.GridPos;
import logical.RiskAwareAgent;
import logical.WumpusWorld;

public class BayesianAgent extends RiskAwareAgent {

	public BayesianAgent(WumpusWorld ww) {
		super(ww);
	}
		
	@Override
	public double risk(GridPos gpos) {
	    return FrontierModel.calcProbPit(oWorld, gpos);
	}
}
