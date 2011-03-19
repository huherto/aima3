/**
 * 
 */
package logical;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


class And extends Sentence {
	List<Sentence> sList = new ArrayList<Sentence>();
    public And(Sentence... sentences) {
    	for(Sentence s: sentences) {
    		sList.add(s);
    	}
    }
    
    public And(String... names) {
    	for(String n: names) {
    		sList.add(new Symbol(n));
    	}
    }

    @Override
    public boolean isTrue(Model model) {
    	for(Sentence s : sList) {
    		if (!s.isTrue(model))
    			return false;
    	}
    	return true;
    }
    
	@Override
	public Set<Symbol> symbols() {
		Set<Symbol> res = new HashSet<Symbol>();
		for(Sentence s : sList) {
			res.addAll(s.symbols());
		}
		return res;
	}
	
	public String makeString() {
		StringBuilder str = new StringBuilder();
		for(Sentence s : sList) {
			if (str.length() > 0)
				str.append(" and ");
			str.append(s.toString());
		}
		
		return "(" + str + ")";
	}
}