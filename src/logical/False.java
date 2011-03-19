/**
 * 
 */
package logical;

import java.util.HashSet;
import java.util.Set;


class False extends Symbol {
    
    public False() {
        super("False");
    }
    
    public boolean isTrue(Model model) {
        return false;
    }
    
	@Override
	public Set<Symbol> symbols() {
		return new HashSet<Symbol>();
	}
}