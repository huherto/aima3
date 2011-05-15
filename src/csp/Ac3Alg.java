package csp;

import java.util.Iterator;
import java.util.List;

public class Ac3Alg {
    
    boolean execute(ConstraintSatisfactionProblem csp, Assignment assignment) {
        List<Arc> queue = csp.allArcs();
        while (!queue.isEmpty()) {
            Arc arc = queue.remove(0);
            if (revise(assignment, arc)) {
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

    private boolean revise(Assignment assignment, Arc arc) {
        boolean revised = false;
        Domain domainFirst  = assignment.domain(arc.first);
        Domain domainSecond = assignment.domain(arc.second);

        Iterator<Value> iter = domainFirst.iterator();
        while (iter.hasNext()) {
            Value v1 = iter.next();
            boolean allowed = false;
            for (Value v2 : domainSecond) {
                if (arc.allowed(v1, v2)) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed) {
                iter.remove();
                revised = true;
            }
        }
        return revised;
    }
}