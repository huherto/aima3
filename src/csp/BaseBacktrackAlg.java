package csp;

import java.util.ArrayList;
import java.util.List;


class BaseBacktrackAlg {
    protected Variable selectUnassignedVariable(ConstraintSatisfactionProblem csp, Assignment assignment) {
        for (Variable var : csp.variables()) {
            if (assignment.get(var) == null)
                return var;
        }
        throw new IllegalAccessError();
    }

    protected List<Value> orderDomainValues(Assignment assignment, Variable var) {
        return new ArrayList<Value>(assignment.domain(var));
    }

    protected List<Inference> findInferences(ConstraintSatisfactionProblem csp, Assignment assignment) {
        for (Arc arc : csp.allArcs()) {
            Value v1 = assignment.get(arc.first);
            Value v2 = assignment.get(arc.second);
            if (v1 != null && !arc.allowed(v1, v2))
                return null;
        }
        return new ArrayList<Inference>();
    }

    protected Assignment backtrack(ConstraintSatisfactionProblem csp, Assignment assignment) {
        // System.out.println(assignment);
        if (assignment.isComplete())
            return assignment;
        Variable var = selectUnassignedVariable(csp, assignment);
        for (Value value : orderDomainValues(assignment, var)) {
//            System.out.println(assignment);
            assignment.add(var, value);
            List<Inference> inferences = findInferences(csp, assignment);
            if (inferences != null) {
                assignment.add(inferences);
                Assignment result = backtrack(csp, assignment);
                if (result != null)
                    return result;
                assignment.remove(inferences);
            }
            assignment.remove(var, value);
        }
        return null; // failure.
    }

    protected Assignment execute(ConstraintSatisfactionProblem csp) {
        Assignment result = backtrack(csp, new Assignment(csp));
        System.out.println(result);
        return result;
    }
}