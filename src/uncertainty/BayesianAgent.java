package uncertainty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logical.GridPos;
import logical.RiskAwareAgent;
import logical.WumpusWorld;

public class BayesianAgent extends RiskAwareAgent {

	public BayesianAgent(WumpusWorld ww) {
		super(ww);
	}

	@SuppressWarnings("serial")
	public class FrontierModel extends HashMap<GridPos,Boolean> {

		public FrontierModel() {
			super();
		}

		public FrontierModel(Map<? extends GridPos, ? extends Boolean> m) {
			super(m);
		}		
	}
	
	public List<FrontierModel> generateFrontierModels(List<GridPos> frontier) {
		
		List<FrontierModel> res = new ArrayList<FrontierModel>();
		if (frontier.isEmpty()) 
			return res;
		
		GridPos first = frontier.get(0);
		
		List<GridPos> rest = frontier.subList(1, frontier.size());
		
		List<FrontierModel> generated = generateFrontierModels(rest);
		
		if (generated.isEmpty()) {
			generated.add(new FrontierModel());
		}
		
		for(FrontierModel model : generated) {
			
			FrontierModel modela = new FrontierModel(model);
			modela.put(first, true);
			res.add(modela);
			
			FrontierModel modelb = new FrontierModel(model);
			modelb.put(first, true);
			res.add(modelb);			
		}
		
		return res;
	}
	
	@Override
	public double risk(GridPos gpos) {
		return 0;
	}

}
