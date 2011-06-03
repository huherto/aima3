package util;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/*
 * Use this to generate all the combinations of a list of elements. 
 * 
 * Usage:
 * 
 * final Set< Set<Symbol> > result = new HashSet< Set<Symbol> >();
 * new Combinator<Symbol>() {
 *   
 *   public void foreach(Set<Symbol> set) {
 *       result.add(new HashSet<Symbol>(set));            
 *   }
 *   
 * }.generate(symbols);
 * 
 * Other usage:
 *   Set< Set<Symbol> > result = (new Combinator<Symbol>()).generateAll(symbols);
 */
public class Combinator<T> {
	
	private Set< Set<T> > result = null;
    
    public void foreach(Set<T> set) {
    	if (result != null) {
    		result.add(set);
    	}
    }
    
    private void generate(List<T> elemList, Set<T> set) {
        
        if (elemList.isEmpty()) {        
       		foreach(set);
        }
        else {
            T head = elemList.get(0);
            List<T> rest = elemList.subList(1, elemList.size());
            generate(rest, set);
            set.add(head);
            generate(rest, set);
            set.remove(head);            
        }
    }
    
    public void generate(Collection<T> symbols) {
        generate(new LinkedList<T>(symbols), new HashSet<T>());
    }
    
    public Set< Set<T> > generateAll(Collection<T> symbols) {
    	result = new HashSet<Set<T>>();
    	generate(symbols);
    	return result;
    }
    
}