package csp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


class Assignment {
    Map<Variable, Value> vars = new HashMap<Variable, Value>();
    Map<Variable, Domain> domains = new HashMap<Variable, Domain>();
    List<Variable> ordered = new LinkedList<Variable>();
    int numVariables; // Number of variables to be assigned.
    
    public Assignment(ConstraintSatisfactionProblem csp) {
        numVariables = csp.variables().size();
        for (Variable var: csp.variables()) {
            Domain nd = new Domain();
            nd.addAll(var.getDomain());
            domains.put(var, nd);
        }
    }

    public Domain domain(Variable var) {
        return domains.get(var);
    }
    
    public void setDomain(Variable var, Value value) {
        Domain d = domains.get(var);
        d.clear();
        d.add(value);
    }

    public void domainRemove(Variable var, Value value) {
        Domain d = domains.get(var);
        d.remove(value);
    }

    public void domainAdd(Variable var, Value value) {
        Domain d = domains.get(var);
        d.add(value);
    }
    
    public void add(Variable var, Value value) {
        vars.put(var, value);
        ordered.add(var);
    }

    public Value get(Variable var) {
        return vars.get(var);
    }

    public void remove(Variable var, Value value) {
        vars.remove(var);
        ordered.remove(var);
    }

    public boolean isComplete() {
        return vars.size() == numVariables;
    }

    public void add(List<Inference> inferences) {
        for (Inference infe : inferences) {
            infe.add(this);
        }
    }

    public void remove(List<Inference> inferences) {
        for (Inference infe : inferences) {
            infe.remove(this);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for(Variable prov: ordered) {
            if (sb.length() > 1)
                sb.append(", ");
            sb.append(prov).append("=").append(vars.get(prov));
        }
        sb.append("}");
        return sb.toString();
    }

}