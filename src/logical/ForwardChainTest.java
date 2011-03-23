package logical;

import junit.framework.TestCase;

public class ForwardChainTest extends TestCase {
    
    public void testBasicFC() {
        ForwardChainKB kb = new ForwardChainKB();
        
        kb.tell(new HornClause("P", "L", "M"));
        kb.tell(new HornClause("M", "B", "L"));
        kb.tell(new HornClause("L", "A", "P"));
        kb.tell(new HornClause("L", "A", "B"));
        kb.tell(HornClause.Fact("A"));
        kb.tell(HornClause.Fact("B"));
        
        assertTrue(kb.entails(new Symbol("L")));
        assertTrue(kb.entails(new Symbol("M")));
        assertTrue(kb.entails(new Symbol("P")));
        assertFalse(kb.entails(new Symbol("Q")));
        
        kb.tell(new HornClause("Q", "P"));
        assertTrue(kb.entails(new Symbol("Q")));
    }

}
