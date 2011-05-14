package csp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import csp.MapColoring.Value;
import csp.MapColoring.Variable;

class Assignment {
    Map<Variable, Value> vars = new HashMap<Variable, Value>();
    Map<Variable, Domain> domains = new HashMap<Variable, Domain>();
    List<Variable> ordered = new LinkedList<Variable>();

    public Assignment() {
        for (Variable p : Variable.values()) {
            domains.put(p, Domain.fullDomain());
        }
    }

    public Domain domain(Variable prov) {
        return domains.get(prov);
    }
    
    public void setDomain(Variable prov, Value color) {
        Domain d = domains.get(prov);
        d.retainAll(null);
        d.add(color);
    }

    public void domainRemove(Variable prov, Value color) {
        Domain d = domains.get(prov);
        d.remove(color);
    }

    public void domainAdd(Variable prov, Value color) {
        Domain d = domains.get(prov);
        d.add(color);
    }
    
    public void add(Variable prov, Value color) {
        vars.put(prov, color);
        ordered.add(prov);
    }

    public Value get(Variable prov) {
        return vars.get(prov);
    }

    public void remove(Variable prov, Value color) {
        vars.remove(prov);
        ordered.remove(prov);
    }

    public boolean isComplete() {
        return vars.size() == 7;
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

    public boolean sameColor(Variable p1, Variable p2) {
        Value c1 = vars.get(p1);
        if (c1 == null)
            return false;
        Value c2 = vars.get(p2);
        return c1.equals(c2);
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