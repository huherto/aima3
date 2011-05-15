package csp;


class NotInDomain implements Inference {
    
    private Variable var;
    private Value value;
    
    public NotInDomain(Variable var, Value value) {
        this.var = var;
        this.value = value;
    }
    
    @Override
    public void add(Assignment assignment) {
        assignment.domainRemove(var, value);
    }

    @Override
    public void remove(Assignment assignment) {
        assignment.domainAdd(var, value);
    }        
}