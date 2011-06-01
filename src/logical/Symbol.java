/**
 * 
 */
package logical;

import java.util.HashSet;
import java.util.Set;


class Symbol extends Sentence {
    public String name;

    public Symbol(String name) {
		this.name = name;
	}

	@Override
    public boolean isTrue(Model model) {
        return model.isTrue(name);
    }

	@Override
	public Set<Symbol> symbols() {
		Set<Symbol> res = new HashSet<Symbol>();
		res.add(this);
		return res;
	}
	
	public String makeString() {
		return name;
	}

    @Override
    public Sentence toCnf() {
        return this;
    }

}