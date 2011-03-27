/**
 * 
 */
package uncertainty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logical.GridPos;
import logical.ObservedWorld;
import logical.ObservedWorld.Square;

@SuppressWarnings("serial")
public class FrontierModel extends HashMap<GridPos,Boolean> {

	public FrontierModel() {
	}

	public FrontierModel(Map<? extends GridPos, ? extends Boolean> m) {
		super(m);
	}		

	public double calcProb() {
		double prob = 1.0;
		for(GridPos pos : keySet()) {
			if (get(pos).equals(true)) {
				prob *= 0.2;
			}
			else {
				prob *= 0.8;
			}
		}
		return prob;
	}
	
	public static List<FrontierModel> generateFrontierModels(List<GridPos> frontier) {
		
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
			modelb.put(first, false);
			res.add(modelb);			
		}
		
		return res;
	}

	public boolean isConsistent(ObservedWorld ow) {
		
		// Check from frontier to ow.
		for(GridPos pos : keySet()) {
			
			if (get(pos).equals(true)) { 
				// All observed neighbors should have breeze.
				for(GridPos npos : ow.observed(ow.neighbors(pos))) {
					Square nbor = ow.at(npos);
					if (!nbor.breeze)
						return false;
				}
			}
		}

		for(GridPos pos : ow.observed(ow.allSquares())) {
			if (ow.at(pos).breeze) {
				// Should have at least one neighbor in frontier with a pit.
				boolean found = false;
				for(GridPos npos : ow.neighbors(pos)) {
					Square nbor = ow.at(npos);
					if (!nbor.observed) {
						
						if (get(npos) == null)
							throw new RuntimeException("illegal state");
						
						if (get(npos).equals(true)) {
							found = true;
							break;
						}						
					}
				}
				if (!found)
					return false;
			}
		}
		
		return true;
	}

	public static double calcProb(ObservedWorld ow, GridPos pos) {	
		double pitProb = 0;
		double noPitProb = 0;

		if (!ow.frontier().contains(pos)) {
			throw new RuntimeException("pos should be in the frontier");
		}
		
		List<FrontierModel> models = generateFrontierModels(ow.frontier());
		
		for(FrontierModel model : models) {
			if (model.isConsistent(ow)) {
				if (model.get(pos).equals(true)) {
					pitProb += model.calcProb();
				}
				else {
					noPitProb += model.calcProb();
				}
			}
		}
		
		double totProb = pitProb + noPitProb;
		if (totProb > 0)
			return pitProb/(pitProb + noPitProb);
		return 0;
	}
	
}