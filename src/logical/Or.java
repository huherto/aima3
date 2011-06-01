/**
 * 
 */
package logical;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

class Or extends Sentence {
	List<Sentence> sList = new ArrayList<Sentence>();
    public Or(Sentence... sentences) {
    	for(Sentence s: sentences) {
    		sList.add(s);
    	}
    }
    
    public Or(String... names) {
    	for(String n: names) {
    		sList.add(new Symbol(n));
    	}
    }

    @Override
    public boolean isTrue(Model model) {
    	for(Sentence s : sList) {
    		if (s.isTrue(model))
    			return true;
    	}
    	return false;
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
				str.append(" or ");
			str.append(paren(s));
		}
		
		return str.toString();
	}

    public List<Sentence> disjuncts() {
        return sList; 
    }
    
    @Override
    public Sentence toCnf() {
        List<Sentence> disjuncts = new LinkedList<Sentence>();
        for(Sentence s: sList) {
            Sentence cnf = s.toCnf();
            if (cnf instanceof Or) {
                disjuncts.addAll(((Or)cnf).disjuncts());
            }
            else {
                disjuncts.add(cnf);
            }
        }
        return new Or(disjuncts.toArray(new Sentence[disjuncts.size()]));
    }
   
}