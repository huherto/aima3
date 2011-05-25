package csp;

import java.util.List;


class OptBacktrackAlg extends BaseBacktrackAlg {
    
    @Override
    protected Variable selectUnassignedVariable(ConstraintSatisfactionProblem csp, Assignment assignment) {
        
        int min = Integer.MAX_VALUE;
        Variable minVar = null;
        for (Variable var : csp.variables()) {
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
    protected List<Inference> findInferences(ConstraintSatisfactionProblem csp, Assignment assignment) {
        List<Inference> res = super.findInferences(csp, assignment);
        if (res == null)
            return null; // failed.
        
        // Forward checking.
        for(Arc arc : csp.allArcs()) {
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