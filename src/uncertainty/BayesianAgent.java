package uncertainty;

import wumpus.GridPos;
import wumpus.RiskAwareAgent;
import wumpus.WumpusWorld;

public class BayesianAgent extends RiskAwareAgent {

	public BayesianAgent(WumpusWorld ww) {
		super(ww);
	}
		
	@Override
	public double risk(GridPos gpos) {
		return FrontierModel.calcProb(oWorld, gpos);
	}
}
