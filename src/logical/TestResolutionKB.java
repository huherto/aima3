package logical;

import java.util.Set;

import junit.framework.TestCase;

public class TestResolutionKB extends TestCase {
	
	public void testStatic() {
		
		
		Or c1 = new Or(new Not("P_2_1"), new Symbol("B_1_1"));
		Or c2 = new Or(new Not("B_1_1"), new Symbol("P_1_2"), new Symbol("P_2_1"));
		Or c3 = new Or(new Not("P_1_2"), new Symbol("B_1_1"));
		Or c4 = new Or(new Not("B_1_1"));
		Or c5 = new Or(new Symbol("P_1_2"));
		
		Set<Or> res = ResolutionKB.resolve(c1, c2);		
		System.out.println(res);
		assertEquals(3, res.size());		
		
		res = ResolutionKB.resolve(c1, c3);
		assertEquals(1, res.size());				
		System.out.println(res);
		
		res = ResolutionKB.resolve(c1, c4);
		assertEquals(1, res.size());				
		System.out.println(res);
		
		res = ResolutionKB.resolve(c2, c3);
		//assertEquals(1, res.size());				
		System.out.println(res);

		res = ResolutionKB.resolve(c2, c4);
		//assertEquals(1, res.size());				
		System.out.println(res);
	}

}
