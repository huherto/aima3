package mx.com.itbrain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class cspMapColoring extends TestCase {

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

    Map<Province, Color> vals = new HashMap<Province, Color>();
    Map<Province, Domain> domains = new HashMap<Province, Domain>();

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

    boolean sameColor(Province p1, Province p2) {
        Color c1 = vals.get(p1);
        if (c1 == null)
            return false;
        Color c2 = vals.get(p2);
        return c1 == c2;
    }

    boolean algoAc3() {
        List<Arc> queue = initialArcs();
        while (!queue.isEmpty()) {
            Arc arc = queue.remove(0);
            if (revise(arc.first, arc.second)) {
                if (domain(arc.first).isEmpty()) {
                    return false;
                }

                for (Arc na : initialArcs()) {
                    if (na.first != arc.second) {
                        if (na.first == arc.first || na.second == arc.first)
                            queue.add(na);
                    }
                }
            }
        }
        return true;
    }

    private Domain domain(Province first) {
        return domains.get(first);
    }

    private boolean revise(Province first, Province second) {
        boolean revised = false;
        Domain domainFirst = domain(first);
        Domain domainSecond = domain(second);

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

    private void initDomains() {
        for (Province p : Province.values()) {
            domains.put(p, Domain.fullDomain());
        }
    }

    private List<Arc> initialArcs() {
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

    void assign(Province prov, Color color) {
        vals.put(prov, color);
        Domain d = domain(prov);

        Domain toRemove = new Domain();
        for (Color c : d) {
            if (c != color) {
                toRemove.add(c);
            }
        }
        d.removeAll(toRemove);
    }

    public void testAlgoAc3() {
        initDomains();
        assign(Province.WA, Color.Red);
        assertEquals(1, domain(Province.WA).size());
        boolean res = algoAc3();
        assertTrue(res);
        assertEquals(1, domain(Province.WA).size());
        assertEquals(2, domain(Province.NT).size());
        assertEquals(2, domain(Province.SA).size());
        assertEquals(3, domain(Province.Q).size());
        assertEquals(3, domain(Province.NSW).size());
        assertEquals(3, domain(Province.V).size());
        assertEquals(3, domain(Province.T).size());

        assign(Province.NT, Color.Blue);
        res = algoAc3();
        assertTrue(res);
        assertEquals(1, domain(Province.WA).size());
        assertEquals(1, domain(Province.NT).size());
        assertEquals(1, domain(Province.SA).size());
        assertEquals(1, domain(Province.Q).size());
        assertEquals(1, domain(Province.NSW).size());
        assertEquals(1, domain(Province.V).size());
        assertEquals(3, domain(Province.T).size());

    }
}
