package csp;

import java.util.ArrayList;
import java.util.List;

import csp.MapColoring.Value;
import csp.MapColoring.Variable;

class BaseBacktrackAlg {
    Variable selectUnassignedVariable(Assignment assignment) {
        for (Variable var : Variable.values()) {
            if (assignment.get(var) == null)
                return var;
        }
        throw new IllegalAccessError();
    }

    List<Value> orderDomainValues(Assignment assignment, Variable var) {
        return new ArrayList<Value>(assignment.domain(var));
    }

    List<Inference> findInferences(Assignment assignment) {
        for (Arc arc : MapColoring.allArcs()) {
            if (assignment.sameColor(arc.first, arc.second))
                return null;
        }
        return new ArrayList<Inference>();
    }

    Assignment backtrack(Assignment assignment) {
        // System.out.println(assignment);
        if (assignment.isComplete())
            return assignment;
        Variable var = selectUnassignedVariable(assignment);
        for (Value value : orderDomainValues(assignment, var)) {
            System.out.println(assignment);
            assignment.add(var, value);
            List<Inference> inferences = findInferences(assignment);
            if (inferences != null) {
                assignment.add(inferences);
                Assignment result = backtrack(assignment);
                if (result != null)
                    return result;
                assignment.remove(inferences);
            }
            assignment.remove(var, value);
        }
        return null; // failure.
    }

    Assignment execute() {
        Assignment result = backtrack(new Assignment());
        System.out.println(result);
        return result;
    }
}