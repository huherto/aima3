/**
 * 
 */
package logical;

import java.util.HashSet;
import java.util.Set;


class True extends Symbol {
    
    True() {
        super("True");
    }
    
    @Override
    public boolean isTrue(Model model) {
        return true;
    }

	@Override
	public Set<Symbol> symbols() {
		return new HashSet<Symbol>();
	}
   	
}