package logical;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class HornClause extends Sentence {
    
    public Symbol head = null;
    public List<Symbol> body = new LinkedList<Symbol>();

    HornClause(Symbol...all) {        
        for(Symbol sym:all) {
            if (head == null)
                head = sym;
            else {
                body.add(sym);
            }
        }
    }
    
    HornClause(String...all) {
        for(String name:all) {
            if (head == null)
                head = new Symbol(name);
            else {
                body.add(new Symbol(name));
            }
        }
    }
    
    static HornClause Fact(String name) {
        return new HornClause(new Symbol(name), new True());
    }
    
    static HornClause NegFact(String name) {
        return new HornClause(new False(), new Symbol(name));
    }
    
    
    @Override
    boolean isTrue(Model model) {
        for(Symbol sym:body) {
            if (!sym.isTrue(model))
                return true;
        }
        // at this point the premise was true.
        return head.isTrue(model);
    }

    @Override
    Set<Symbol> symbols() {
        Set<Symbol> set = new HashSet<Symbol>();
        set.add(head);
        set.addAll(body);
        return set;
    }

    @Override
    protected String makeString() {
        StringBuilder str = new StringBuilder();
        for(Symbol sym : body) {
            if (str.length() > 0)
                str.append(" and ");
            str.append(sym.toString());
        }
        str.append(" => ").append(head);
        return str.toString();
    }

}
