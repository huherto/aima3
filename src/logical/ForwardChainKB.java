package logical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


public class ForwardChainKB {
    
    Set<HornClause> clauses = new HashSet<HornClause>();
    
    public void tell(HornClause sentence) {
        System.out.println(sentence);
        clauses.add(sentence);
    }

    public boolean query(String n) {
        return entails(new Symbol(n));
    }
    
    
    public boolean entails(Symbol query) {
        
        class Counter {
            public int value;        
            public Counter(int v) {
                value = v;
            }
            public String toString() { 
                return "Counter("+value+")";
            }
        }
        
        HashMap<HornClause, Counter> count = new HashMap<HornClause, Counter>();
        Queue<Symbol> agenda = new LinkedList<Symbol>();
        Set<Symbol> allSymbols = new HashSet<Symbol>();
        for(HornClause clause : clauses) {
            allSymbols.addAll(clause.symbols());
            count.put(clause, new Counter(clause.symbolsOnBody().size()));
            if (clause.isFact())
                agenda.add(clause.head);
        }
        
        HashMap<Symbol, Boolean> inferred = new HashMap<Symbol, Boolean>();
        for(Symbol sym: allSymbols) {
            inferred.put(sym, false);
        }
        
        while(!agenda.isEmpty()) {
            Symbol p = agenda.remove();
            if (p .equals(query)) {
                return true;
            }
            if (!inferred.get(p)) {
                inferred.put(p, true);
                for(HornClause c: clauses) {
                    if (c.body.contains(p)) {
                        Counter counter = count.get(c);
                        counter.value--;
                        if (counter.value == 0)
                            agenda.add(c.head);
                    }
                }
            }
        }
        return false;
    }
}