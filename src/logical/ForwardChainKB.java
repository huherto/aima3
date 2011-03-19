package logical;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


class ForwardChainKB {
    
    Set<HornClause> clauses = new HashSet<HornClause>();
    
    void tell(HornClause sentence) {
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
        }
        
        Set<Symbol> allSymbols = new HashSet<Symbol>();
        
        HashMap<HornClause, Counter> count = new HashMap<HornClause, Counter>();
        for(HornClause clause : clauses) {
            Set<Symbol> syms = clause.symbols();
            count.put(clause, new Counter(syms.size()));
        }
        
        HashMap<Symbol, Boolean> inferred = new HashMap<Symbol, Boolean>();
        for(Symbol sym: allSymbols) {
            inferred.put(sym, false);
        }
        
        List<Symbol> agenda = new LinkedList<Symbol>(allSymbols);
        while(!agenda.isEmpty()) {
            Symbol p = agenda.remove(0);
            if (p == query) {
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