/**
 * 
 */
package logical;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


class TruthTableKB {
    
    Set<Sentence> sentences = new HashSet<Sentence>();
    
    void tell(Sentence sentence) {
        System.out.println(sentence);
        sentences.add(sentence);
    }

    public boolean query(String n) {
    	return query(new Symbol(n));
    }
    
    public boolean query(Sentence alpha) {
    	
        if (sentences.contains(alpha))
            return true;
        
    	Set<Symbol> symbols = new HashSet<Symbol>();
    	for(Sentence sentence : sentences) {
    		symbols.addAll(sentence.symbols());
    	}
    	symbols.addAll(alpha.symbols());
    	
    	return ttCheckAll(alpha, new LinkedList<Symbol>(symbols), new Model());
    }

	private boolean ttCheckAll(Sentence alpha, List<Symbol> symbols, Model model) {
		
		if (symbols.isEmpty()) {
			if (isTrue(model)) {
                return alpha.isTrue(model);				    
			}
			else
				return true;
		}
		else {
			Symbol P = symbols.get(0);
			List<Symbol> rest = symbols.subList(1, symbols.size());
			
			boolean result = false;
			
			model.set(P, true);								
			if (ttCheckAll(alpha, rest, model)) {
				model.set(P, false);
				if (ttCheckAll(alpha, rest, model)) {
					result = true;
				}
			}
			model.unset(P);
			return result;
		}
	}

	private boolean isTrue(Model model) {
		for(Sentence s : sentences) {
			if (!s.isTrue(model))
				return false;
		}
		return true;
	}
    
}