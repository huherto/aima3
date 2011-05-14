package csp;

import java.util.List;

import csp.MapColoring.Value;
import csp.MapColoring.Variable;

class OptBacktrackAlg extends BaseBacktrackAlg {
    
    @Override
    Variable selectUnassignedVariable(Assignment assignment) {
        
        int min = Integer.MAX_VALUE;
        Variable minVar = null;
        for (Variable var : Variable.values()) {
            if (assignment.get(var) == null) {
                int size = assignment.domain(var).size(); 
                if (size < min) {
                    min = size;
                    minVar = var;
                }
            }
        }
        if (minVar == null)
            throw new IllegalAccessError();
        return minVar;
    }
    
    @Override
    List<Inference> findInferences(Assignment assignment) {
        List<Inference> res = super.findInferences(assignment);
        if (res == null)
            return null; // failed.
        
        // Forward checking.
        for(Arc arc : MapColoring.allArcs()) {
            Value var1 = assignment.get(arc.first);
            if (var1 != null) {
                if (assignment.domain(arc.second).contains(var1)) {
                    res.add(new NotInDomain(arc.second, var1));
                }
            }
        }
        
        return res;
    }
}