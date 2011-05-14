package csp;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class MapColoring extends TestCase {

    enum Value {
        Red, Green, Blue
    };

    enum Variable {
        WA, NT, Q, NSW, V, SA, T
    };

    public static List<Arc> allArcs() {
        List<Arc> arcs = new LinkedList<Arc>();
        arcs.add(new Arc(Variable.WA, Variable.NT));
        arcs.add(new Arc(Variable.WA, Variable.SA));
        arcs.add(new Arc(Variable.NT, Variable.SA));
        arcs.add(new Arc(Variable.NT, Variable.Q));
        arcs.add(new Arc(Variable.SA, Variable.Q));
        arcs.add(new Arc(Variable.SA, Variable.NSW));
        arcs.add(new Arc(Variable.SA, Variable.V));
        arcs.add(new Arc(Variable.Q, Variable.NSW));
        arcs.add(new Arc(Variable.NSW, Variable.V));

        arcs.add(new Arc(Variable.NT, Variable.WA));
        arcs.add(new Arc(Variable.SA, Variable.WA));
        arcs.add(new Arc(Variable.SA, Variable.NT));
        arcs.add(new Arc(Variable.Q, Variable.NT));
        arcs.add(new Arc(Variable.Q, Variable.SA));
        arcs.add(new Arc(Variable.NSW, Variable.SA));
        arcs.add(new Arc(Variable.V, Variable.SA));
        arcs.add(new Arc(Variable.NSW, Variable.Q));
        arcs.add(new Arc(Variable.V, Variable.NSW));

        return arcs;
    }
    
    boolean algoAc3(Assignment assignment) {
        List<Arc> queue = allArcs();
        while (!queue.isEmpty()) {
            Arc arc = queue.remove(0);
            if (revise(assignment, arc.first, arc.second)) {
                if (assignment.domain(arc.first).isEmpty()) {
                    return false;
                }

                for (Arc na : allArcs()) {
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
            Value colorFirst = iter.next();
            boolean allowed = false;
            for (Value colorSecond : domainSecond) {
                if (colorFirst != colorSecond)
                    allowed = true;
            }
            if (!allowed) {
                iter.remove();
                revised = true;
            }
        }
        return revised;
    }

    public void testAlgoAc3() {
        Assignment a = new Assignment();
        a.add(Variable.WA, Value.Red);
        a.setDomain(Variable.WA, Value.Red);
        assertEquals(1, a.domain(Variable.WA).size());
        boolean res = algoAc3(a);
        assertTrue(res);
        assertEquals(1, a.domain(Variable.WA).size());
        assertEquals(2, a.domain(Variable.NT).size());
        assertEquals(2, a.domain(Variable.SA).size());
        assertEquals(3, a.domain(Variable.Q).size());
        assertEquals(3, a.domain(Variable.NSW).size());
        assertEquals(3, a.domain(Variable.V).size());
        assertEquals(3, a.domain(Variable.T).size());

        a.add(Variable.NT, Value.Blue);
        a.setDomain(Variable.NT, Value.Blue);
        res = algoAc3(a);
        assertTrue(res);
        assertEquals(1, a.domain(Variable.WA).size());
        assertEquals(1, a.domain(Variable.NT).size());
        assertEquals(1, a.domain(Variable.SA).size());
        assertEquals(1, a.domain(Variable.Q).size());
        assertEquals(1, a.domain(Variable.NSW).size());
        assertEquals(1, a.domain(Variable.V).size());
        assertEquals(3, a.domain(Variable.T).size());
        assertTrue(a.domain(Variable.SA).contains(Value.Green));
        assertTrue(a.domain(Variable.Q).contains(Value.Red));
        assertTrue(a.domain(Variable.NSW).contains(Value.Blue));
        assertTrue(a.domain(Variable.V).contains(Value.Red));
    }

    public void testBacktrackSearch() {
        BaseBacktrackAlg alg = new BaseBacktrackAlg(); 
        Assignment result = alg.execute();
        assertTrue(result.isComplete());
        assertNotNull(alg.findInferences(result));
        
        alg = new OptBacktrackAlg(); 
        result = alg.execute();
        assertTrue(result.isComplete());
        assertNotNull(alg.findInferences(result));

    }
}
