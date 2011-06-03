package logical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
        for(HornClause clause : clauses) {
            Set<Symbol> syms = clause.symbolsOnBody();
            count.put(clause, new Counter(syms.size()));
        }
        
        Set<Symbol> allSymbols = new HashSet<Symbol>();
        for(HornClause clause : clauses) {
            Set<Symbol> syms = clause.symbols();
            allSymbols.addAll(syms);
        }
        
        HashMap<Symbol, Boolean> inferred = new HashMap<Symbol, Boolean>();
        inferred.put(new True(), true);
        for(Symbol sym: allSymbols) {
            inferred.put(sym, false);
        }
        
        List<Symbol> agenda = new LinkedList<Symbol>();
        agenda.add(new True());
        
        while(!agenda.isEmpty()) {
            Symbol p = agenda.remove(0);
            if (p .equals(query)) {
                return true;
            }
            if (inferred.get(p) == false) {
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