package logical;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/*
 * Use this to generate all the combinations of a list of elements. 
 */
public abstract class Combinator<T> {
    
    public abstract void foreach(Set<T> set);
    
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
    
    public void generate(List<T> symbols) {
        generate(symbols, new HashSet<T>());
    }
}