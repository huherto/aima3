package csp;

import java.util.List;

public interface ConstraintSatisfactionProblem {

    List<Variable> variables();

    List<Arc> allArcs();

}
