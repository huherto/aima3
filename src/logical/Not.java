/**
 * 
 */
package logical;

import java.util.HashSet;
import java.util.Set;

class Not extends Sentence {
    private Sentence s;
    public Not(Sentence s) {
        this.s = s;
    }
    public Not(String name) {
        this.s = new Symbol(name);
    }
    
    @Override
    public boolean isTrue(Model model) {
        return !s.isTrue(model);
    }            
    
	@Override
	public Set<Symbol> symbols() {
		Set<Symbol> res = new HashSet<Symbol>();
		res.addAll(s.symbols());
		return res;
	}
	
	public String makeString() {
		return "(¬"+s+")";
	}
}