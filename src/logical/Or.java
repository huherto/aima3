/**
 * 
 */
package logical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import util.StringUtil;

public class Or extends Sentence {
	
	List<Sentence> sList = new ArrayList<Sentence>();
	
    public Or(Sentence... sentences) {
    	for(Sentence s: sentences) {
    		sList.add(s);
    	}
    }
    
    public Or(Collection<Sentence> sentences) {
    	sList.addAll(sentences);
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
		List<String> list = new ArrayList<String>();
		for(Sentence s : sList) {
			list.add(paren(s));			
		}
		Collections.sort(list);
		return StringUtil.join(list, " or ");		
	}

    public List<Sentence> disjuncts() {
        return sList; 
    }
    
    @Override
    public Sentence toCnf() {
    	And and = null; // In case there is an and inside this or.
        List<Sentence> disjuncts = new LinkedList<Sentence>();
        for(Sentence s: sList) {
            Sentence cnf = s.toCnf();
            if (cnf instanceof Or) {
                disjuncts.addAll(((Or)cnf).disjuncts());
            }
            else if (cnf instanceof And) {
            	if (and != null) {
            		throw new RuntimeException("Do not how to handle more than one and!");
            	}
            	and = (And) cnf;
            }
            else {
                disjuncts.add(cnf);
            }
        }
        
        if (and != null) {
        	
        	List<Sentence> conjuncts = new LinkedList<Sentence>();
        	for(Sentence conj : and.conjuncts()) {
        		disjuncts.add(0, conj);
        		conjuncts.add(new Or(disjuncts));        		
        		disjuncts.remove(0);
        	}
        	
        	return new And(conjuncts).toCnf();
        	
        }
        
        return new Or(disjuncts);
    }
   
}