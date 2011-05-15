package csp;

import java.util.Iterator;
import java.util.List;

public class Ac3Alg {
    
    boolean execute(ConstraintSatisfactionProblem csp, Assignment assignment) {
        List<Arc> queue = csp.allArcs();
        while (!queue.isEmpty()) {
            Arc arc = queue.remove(0);
            if (revise(assignment, arc.first, arc.second)) {
                if (assignment.domain(arc.first).isEmpty()) {
                    return false;
                }

                for (Arc na : csp.allArcs()) {
                    if (na.first != arc.second) {
                        if (na.first == arc.first || na.second == arc.first)
                            queue.add(na);
                    }
                }
            }
        }
        return true;
    }

    private boolean revise(Assignment assignment, Variable first,
            Variable second) {
        boolean revised = false;
        Domain domainFirst = assignment.domain(first);
        Domain domainSecond = assignment.domain(second);

        Iterator<Value> iter = domainFirst.iterator();
        while (iter.hasNext()) {
            Value valueFirst = iter.next();
            boolean allowed = false;
            for (Value valueSecond : domainSecond) {
                if (valueFirst != valueSecond)
                    allowed = true;
            }
            if (!allowed) {
                iter.remove();
                revised = true;
            }
        }
        return revised;
    }
}