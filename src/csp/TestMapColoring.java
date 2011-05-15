package csp;


import junit.framework.TestCase;

public class TestMapColoring extends TestCase {

    public void testAlgoAc3() {
        
        MapColoring mc = new MapColoring();

        Assignment a = new Assignment(mc);
        a.add(MapColoring.WA, MapColoring.RED);
        a.setDomain(MapColoring.WA, MapColoring.RED);
        assertEquals(1, a.domain(MapColoring.WA).size());
        
        Ac3Alg ac3 = new Ac3Alg();
        
        boolean res = ac3.execute(mc, a);
        
        assertTrue(res);
        assertEquals(1, a.domain(MapColoring.WA).size());
        assertEquals(2, a.domain(MapColoring.NT).size());
        assertEquals(2, a.domain(MapColoring.SA).size());
        assertEquals(3, a.domain(MapColoring.Q).size());
        assertEquals(3, a.domain(MapColoring.NSW).size());
        assertEquals(3, a.domain(MapColoring.V).size());
        assertEquals(3, a.domain(MapColoring.T).size());

        a.add(MapColoring.NT, MapColoring.BLUE);
        a.setDomain(MapColoring.NT, MapColoring.BLUE);
        res = ac3.execute(mc, a);
        assertTrue(res);
        assertEquals(1, a.domain(MapColoring.WA).size());
        assertEquals(1, a.domain(MapColoring.NT).size());
        assertEquals(1, a.domain(MapColoring.SA).size());
        assertEquals(1, a.domain(MapColoring.Q).size());
        assertEquals(1, a.domain(MapColoring.NSW).size());
        assertEquals(1, a.domain(MapColoring.V).size());
        assertEquals(3, a.domain(MapColoring.T).size());
        assertTrue(a.domain(MapColoring.SA).contains(MapColoring.GREEN));
        assertTrue(a.domain(MapColoring.Q).contains(MapColoring.RED));
        assertTrue(a.domain(MapColoring.NSW).contains(MapColoring.BLUE));
        assertTrue(a.domain(MapColoring.V).contains(MapColoring.RED));
    }

    public void testBacktrackSearch() {
        
        MapColoring mc = new MapColoring();

        BaseBacktrackAlg alg = new BaseBacktrackAlg(); 
        Assignment result = alg.execute(mc);
        assertTrue(result.isComplete());
        assertNotNull(alg.findInferences(mc, result));
        
        alg = new OptBacktrackAlg(); 
        result = alg.execute(mc);
        assertTrue(result.isComplete());
        assertNotNull(alg.findInferences(mc, result));

    }
}
