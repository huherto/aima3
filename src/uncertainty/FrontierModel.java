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

@SuppressWarnings("serial")
public class FrontierModel extends HashMap<GridPos,Boolean> {

	public FrontierModel() {
	}

	public FrontierModel(Map<? extends GridPos, ? extends Boolean> m) {
		super(m);
	}

    public boolean isConsistent(ObservedWorld oWorld) {
        // TODO Auto-generated method stub
        return false;
    }

    public double calcProb() {
        
        double prob = 1.0;
        for(GridPos pos : this.keySet()) {
            if (get(pos) == true) {
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
    
    public static double calcProbPit(ObservedWorld oWorld, GridPos pos) {
        
        double pitProb = 0;
        double noPitProb = 0;
        List<FrontierModel> models = generateFrontierModels(oWorld.frontier());
        for(FrontierModel model: models) {
            if (model.isConsistent(oWorld)) {
                if (model.get(pos) == true) {
                    pitProb   += model.calcProb();
                }
                else if (model.get(pos) == false) {
                    noPitProb += model.calcProb();
                }               
            }
        }
        
        return (pitProb / (pitProb + noPitProb));
    }

}