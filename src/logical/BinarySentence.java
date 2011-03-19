/**
 * 
 */
package logical;

import java.util.HashSet;
import java.util.Set;


abstract class BinarySentence extends Sentence {
    protected Sentence s1;
    protected Sentence s2;
    public BinarySentence(Sentence s1, Sentence s2) {
        this.s1 = s1;
        this.s2 = s2;
    }
    
	@Override
	public Set<Symbol> symbols() {
		Set<Symbol> res = new HashSet<Symbol>();
		res.addAll(s1.symbols());
		res.addAll(s2.symbols());
		return res;
	}
}