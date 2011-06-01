/**
 * 
 */
package logical;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Not extends Sentence {
    private Sentence s;
    public Not(Sentence s) {
        this.s = s;
    }
    public Not(String name) {
        this.s = new Symbol(name);
    }
    
    @Override
    public boolean isTrue(Model model) {
        return !s.isTrue(model);
    }            
    
	@Override
	public Set<Symbol> symbols() {
		Set<Symbol> res = new HashSet<Symbol>();
		res.addAll(s.symbols());
		return res;
	}
	
	public String makeString() {
		return "¬" + paren(s);
	}
	
    @Override
    public Sentence toCnf() {
        if (s instanceof Not) {
            return ((Not)s).sentence().toCnf();
        }
        else if (s instanceof And) {
            List<Sentence> cons = ((And) s).conjuncts();
            Sentence dis[] = new Sentence[cons.size()];
            int i = 0;
            for(Sentence s : cons) {
                dis[i] = new Not(s).toCnf();
                i++;
            }
            return new Or(dis);
        }
        else if (s instanceof Or) {
            List<Sentence> dis = ((Or) s).disjuncts();
            Sentence list[] = new Sentence[dis.size()];
            int i = 0;
            for(Sentence s : dis) {
                list[i] = new Not(s).toCnf();
                i++;
            }
            return new And(list);
        }
        return new Not(s.toCnf());
    }
    
    private Sentence sentence() {
        return s;
    }
}