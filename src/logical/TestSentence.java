package logical;

import junit.framework.TestCase;

public class TestSentence extends TestCase {
    
    public void testBasicPrint() {
        
        Sentence notP = new Not("P");
        assertEquals("¬P", notP.toString());
        
        Sentence impl = new If(new Symbol("P"), new Symbol("Q"));
        assertEquals("P => Q", impl.toString());
        
        Sentence or = new Or(new Symbol("P"), new Symbol("Q"));
        assertEquals("P or Q", or.toString());
        
        Sentence and = new And(new Symbol("P"), new Symbol("Q"), new Symbol("R"));
        assertEquals("P and Q and R", and.toString());
        
        Sentence dimpl = new Iff(new Symbol("P"), new Symbol("Q"));
        assertEquals("P <=> Q", dimpl.toString());
        
        and = new And(or, new Symbol("R"));
        assertEquals("(P or Q) and R", and.toString());

        impl = new If(or, or);
        assertEquals("(P or Q) => (P or Q)", impl.toString());
        
        notP = new Not(and);
        assertEquals("¬((P or Q) and R)", notP.toString());
        
        or = new Or(and, new Symbol("P"));
        assertEquals("((P or Q) and R) or P", or.toString());
        
    }
    
    public void testBasicCnf() {
        Sentence r1 = new If(new Symbol("a"), new Symbol("B"));
        assertEquals("a => B", r1.toString());
        assertEquals("¬a or B", r1.toCnf().toString());
        
        Sentence r2 = new Not(new Not("a"));
        assertEquals("¬¬a", r2.toString());
        assertEquals("a", r2.toCnf().toString());
        
        Sentence r3 = new Not(new And(new Symbol("a"), new Symbol("B")));
        assertEquals("¬(a and B)", r3.toString());
        assertEquals("¬a or ¬B", r3.toCnf().toString());
        
        Sentence r4 = new Not(new Or(new Symbol("a"), new Symbol("B")));
        assertEquals("¬(a or B)", r4.toString());
        assertEquals("¬a and ¬B", r4.toCnf().toString());
        
        Sentence r5 = new Iff(new Symbol("a"), new Symbol("B"));
        assertEquals("a <=> B", r5.toString());
        assertEquals("(¬a or B) and (¬B or a)", r5.toCnf().toString());
        
        Sentence r6 = new Or(new Symbol("a"), new Or(new Symbol("b"), new Symbol("c")));
        assertEquals("a or (b or c)", r6.toString());
        assertEquals("a or b or c", r6.toCnf().toString());
        
        Sentence r7 = new And(new Symbol("a"), new And(new Symbol("b"), new Symbol("c")));
        assertEquals("a and (b and c)", r7.toString());
        assertEquals("a and b and c", r7.toCnf().toString());
        
        Sentence r8 = new Or(new And(new Symbol("a"), new Symbol("b")), new Symbol("c"));
        assertEquals("(a and b) or c", r8.toString());
        assertEquals("(a or c) and (b or c)", r8.toCnf().toString());

    }
    
    public void testComplex() {
        
        // this is not working yet.
        Sentence r1 = new Iff(new Symbol("B_1_1"), new Or(new Symbol("P_1_2"), new Symbol("P_2_1")));
        assertEquals("B_1_1 <=> (P_1_2 or P_2_1)", r1.toString());
        assertEquals("(¬B_1_1 or P_1_2 or P_2_1) and (¬P_1_2 or B_1_1) and (¬P_2_1 or B_1_1)", r1.toCnf().toString());
        
    }

}
