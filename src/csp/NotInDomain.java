package csp;

import csp.MapColoring.Value;
import csp.MapColoring.Variable;

class NotInDomain implements Inference {
    
    Variable prov;
    Value color;
    
    public NotInDomain(Variable p, Value c) {
        prov = p;
        color = c;
    }
    
    @Override
    public void add(Assignment assignment) {
        assignment.domainRemove(prov, color);
    }

    @Override
    public void remove(Assignment assignment) {
        assignment.domainAdd(prov, color);
    }        
}