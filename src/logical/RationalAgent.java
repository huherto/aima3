package logical;

import java.util.Collections;
import java.util.List;

public class RationalAgent extends Agent {

    class Square {
        boolean observed = false;
        boolean breeze = false;
    };
    
    class ObservedWorld {
        Square map[][];
        
        int size;
        
        public ObservedWorld(int size) {
            this.size = size;
            map = new Square[size][size];
                        
            for(int i = 0; i < size; i++) {
                for(int j = 0; j < size; j++) {
                    map[i][j] = new Square();
                }
            }
        }

        public Square at(GridPos pos) {
            return map[pos.x][pos.y];
        }
    }
    
    private ObservedWorld oWorld;
    
    public RationalAgent(WumpusWorld ww) {
        super(ww);
        oWorld = new ObservedWorld(ww.size);
    }

    @Override
    public Action nextAction(Percept percept) {
        // Update the observed world.
        Square current = oWorld.at(pos);        
        current.observed = true;
        current.breeze = percept.breeze;
        
        List<GridPos> nbors = neighbors(pos);
        Collections.shuffle(nbors);
        for(GridPos npos: nbors) {
            if (!oWorld.at(npos).observed && isSafe(npos)) {
                System.out.println("Safe move");
                return getMoveDir(npos);
            }
        }
        
        if (thereIsSafe()) {
            for(GridPos npos: nbors) {
                if (oWorld.at(npos).observed) {
                    System.out.println("Coward move");
                    return getMoveDir(npos);
                }
            }
        }
    
        GridPos minRiskPos = null;
        double minRisk = 100;
        for(GridPos npos: nbors) {
            if (!oWorld.at(npos).observed) {                
                double risk = risk(npos);
                if (risk < minRisk) {
                    minRisk = risk;
                    minRiskPos = npos;
                }
            }
        }
        
        if (minRisk < .5) {
            System.out.println("Risky move "+minRisk);
            return getMoveDir(minRiskPos);            
        }
        
        for(GridPos npos: nbors) {
            if (oWorld.at(npos).observed) {
                System.out.println("Coward move");
                return getMoveDir(npos);
            }
        }
        
        return getMoveDir(nbors.get(0));
    }

    private boolean thereIsSafe() {
        for(int i = 0; i < ww.size; i++) {
            for(int j = 0; j < ww.size; j++) {
                if (isSafe(new GridPos(i, j)))
                    return true;
            }
        }
        return false;
    }
    
    private boolean isSafe(GridPos gpos) {
        List<GridPos> nbors = neighbors(gpos);
        for(GridPos npos : nbors ) {
            if (oWorld.at(npos).observed && !oWorld.at(npos).breeze)
                return true;
        }
        return false;
    }
    
    private double risk(GridPos gpos) {
        double risk = 0;
        List<GridPos> nbors = neighbors(gpos);
        int numNbors = nbors.size();
        for(GridPos npos : nbors ) {
            if (oWorld.at(npos).observed) {
                if (oWorld.at(npos).breeze)
                    risk += 1 / neighbors(npos).size();
                else 
                    return 0;
            }      
            else {
                risk += .20 / numNbors;
            }
        }
        return risk;
    }

}
