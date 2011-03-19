package csp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class MapColoring extends TestCase {

    enum Color {
        Red, Green, Blue
    };

    enum Province {
        WA, NT, Q, NSW, V, SA, T
    };

    @SuppressWarnings("serial")
    static class Domain extends HashSet<Color> {
        static Domain fullDomain() {
            Domain d = new Domain();
            d.addAll(Arrays.asList(Color.values()));
            return d;
        }

    }

    static class Arc {
        Province first;
        Province second;

        public Arc(Province p1, Province p2) {
            first = p1;
            second = p2;
        }

        public String toString() {
            return "(" + first + "," + second + ")";
        }
    };

    static class Assignment {
        Map<Province, Color> vars = new HashMap<Province, Color>();
        Map<Province, Domain> domains = new HashMap<Province, Domain>();
        List<Province> ordered = new LinkedList<Province>();

        public Assignment() {
            for (Province p : Province.values()) {
                domains.put(p, Domain.fullDomain());
            }
        }

        public Domain domain(Province prov) {
            return domains.get(prov);
        }
        
        public void setDomain(Province prov, Color color) {
            Domain d = domains.get(prov);
            d.retainAll(null);
            d.add(color);
        }

        public void domainRemove(Province prov, Color color) {
            Domain d = domains.get(prov);
            d.remove(color);
        }

        public void domainAdd(Province prov, Color color) {
            Domain d = domains.get(prov);
            d.add(color);
        }
        
        public void add(Province prov, Color color) {
            vars.put(prov, color);
            ordered.add(prov);
        }

        public Color get(Province prov) {
            return vars.get(prov);
        }

        public void remove(Province prov, Color color) {
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

        public boolean sameColor(Province p1, Province p2) {
            Color c1 = vars.get(p1);
            if (c1 == null)
                return false;
            Color c2 = vars.get(p2);
            return c1.equals(c2);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for(Province prov: ordered) {
                if (sb.length() > 1)
                    sb.append(", ");
                sb.append(prov).append("=").append(vars.get(prov));
            }
            sb.append("}");
            return sb.toString();
        }

    }

    interface Inference {

        void add(Assignment assignment);

        void remove(Assignment assignment);
    }
    
    static class NotInDomain implements Inference {
        
        Province prov;
        Color color;
        
        public NotInDomain(Province p, Color c) {
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

    public static List<Arc> allArcs() {
        List<Arc> arcs = new LinkedList<Arc>();
        arcs.add(new Arc(Province.WA, Province.NT));
        arcs.add(new Arc(Province.WA, Province.SA));
        arcs.add(new Arc(Province.NT, Province.SA));
        arcs.add(new Arc(Province.NT, Province.Q));
        arcs.add(new Arc(Province.SA, Province.Q));
        arcs.add(new Arc(Province.SA, Province.NSW));
        arcs.add(new Arc(Province.SA, Province.V));
        arcs.add(new Arc(Province.Q, Province.NSW));
        arcs.add(new Arc(Province.NSW, Province.V));

        arcs.add(new Arc(Province.NT, Province.WA));
        arcs.add(new Arc(Province.SA, Province.WA));
        arcs.add(new Arc(Province.SA, Province.NT));
        arcs.add(new Arc(Province.Q, Province.NT));
        arcs.add(new Arc(Province.Q, Province.SA));
        arcs.add(new Arc(Province.NSW, Province.SA));
        arcs.add(new Arc(Province.V, Province.SA));
        arcs.add(new Arc(Province.NSW, Province.Q));
        arcs.add(new Arc(Province.V, Province.NSW));

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

    private boolean revise(Assignment assignment, Province first,
            Province second) {
        boolean revised = false;
        Domain domainFirst = assignment.domain(first);
        Domain domainSecond = assignment.domain(second);

        Iterator<Color> iter = domainFirst.iterator();
        while (iter.hasNext()) {
            Color colorFirst = iter.next();
            boolean allowed = false;
            for (Color colorSecond : domainSecond) {
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
        a.add(Province.WA, Color.Red);
        a.setDomain(Province.WA, Color.Red);
        assertEquals(1, a.domain(Province.WA).size());
        boolean res = algoAc3(a);
        assertTrue(res);
        assertEquals(1, a.domain(Province.WA).size());
        assertEquals(2, a.domain(Province.NT).size());
        assertEquals(2, a.domain(Province.SA).size());
        assertEquals(3, a.domain(Province.Q).size());
        assertEquals(3, a.domain(Province.NSW).size());
        assertEquals(3, a.domain(Province.V).size());
        assertEquals(3, a.domain(Province.T).size());

        a.add(Province.NT, Color.Blue);
        a.setDomain(Province.NT, Color.Blue);
        res = algoAc3(a);
        assertTrue(res);
        assertEquals(1, a.domain(Province.WA).size());
        assertEquals(1, a.domain(Province.NT).size());
        assertEquals(1, a.domain(Province.SA).size());
        assertEquals(1, a.domain(Province.Q).size());
        assertEquals(1, a.domain(Province.NSW).size());
        assertEquals(1, a.domain(Province.V).size());
        assertEquals(3, a.domain(Province.T).size());
        assertTrue(a.domain(Province.SA).contains(Color.Green));
        assertTrue(a.domain(Province.Q).contains(Color.Red));
        assertTrue(a.domain(Province.NSW).contains(Color.Blue));
        assertTrue(a.domain(Province.V).contains(Color.Red));
    }

    static class BaseBacktrackAlg {
        Province selectUnassignedVariable(Assignment assignment) {
            for (Province prov : Province.values()) {
                if (assignment.get(prov) == null)
                    return prov;
            }
            throw new IllegalAccessError();
        }
    
        List<Color> orderDomainValues(Assignment assignment, Province prov) {
            return new ArrayList<Color>(assignment.domain(prov));
        }
    
        List<Inference> findInferences(Assignment assignment) {
            for (Arc arc : allArcs()) {
                if (assignment.sameColor(arc.first, arc.second))
                    return null;
            }
            return new ArrayList<Inference>();
        }
    
        Assignment backtrack(Assignment assignment) {
            // System.out.println(assignment);
            if (assignment.isComplete())
                return assignment;
            Province prov = selectUnassignedVariable(assignment);
            for (Color color : orderDomainValues(assignment, prov)) {
                System.out.println(assignment);
                assignment.add(prov, color);
                List<Inference> inferences = findInferences(assignment);
                if (inferences != null) {
                    assignment.add(inferences);
                    Assignment result = backtrack(assignment);
                    if (result != null)
                        return result;
                    assignment.remove(inferences);
                }
                assignment.remove(prov, color);
            }
            return null; // failure.
        }
    
        Assignment execute() {
            Assignment result = backtrack(new Assignment());
            System.out.println(result);
            return result;
        }
    }
    
    static class OptBacktrackAlg extends BaseBacktrackAlg {
        
        @Override
        Province selectUnassignedVariable(Assignment assignment) {
            
            int min = Integer.MAX_VALUE;
            Province minVar = null;
            for (Province prov : Province.values()) {
                if (assignment.get(prov) == null) {
                    int size = assignment.domain(prov).size(); 
                    if (size < min) {
                        min = size;
                        minVar = prov;
                    }
                }
            }
            if (minVar == null)
                throw new IllegalAccessError();
            return minVar;
        }
        
        @Override
        List<Inference> findInferences(Assignment assignment) {
            List<Inference> res = super.findInferences(assignment);
            if (res == null)
                return null; // failed.
            
            // Forward checking.
            for(Arc arc : allArcs()) {
                Color c1 = assignment.get(arc.first);
                if (c1 != null) {
                    if (assignment.domain(arc.second).contains(c1)) {
                        res.add(new NotInDomain(arc.second, c1));
                    }
                }
            }
            
            return res;
        }
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
