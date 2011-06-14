/**
 * 
 */
package logical;

import java.util.Set;


abstract public class Sentence {
    
    private String str = null;
    
    abstract boolean isTrue(Model model);
    
	abstract Set<Symbol> symbols();
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Sentence))
			return false;
		if (this == other)
			return true;
		
		// Convert to a canonical representation to be able to compare.
		String s1 = toString();
		String s2 = other.toString();
		return s1.equals(s2);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();			
	}
	
	public final String toString() {
	    if (str == null)
	        str = makeString();
	    return str;
	}

    protected abstract String makeString();
    
    protected static String paren(Sentence s) {
        if (s instanceof Not || s instanceof Symbol) {
            return s.toString();
        }
        else {
            return "(" + s + ")";
        }
    }

    public abstract Sentence toCnf();
    
}