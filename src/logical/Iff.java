/**
 * 
 */
package logical;


class Iff extends BinarySentence {
    public Iff(Sentence s1, Sentence s2) {
        super(s1, s2);
    }

    @Override
    public boolean isTrue(Model model) {
    	return s1.isTrue(model) == s2.isTrue(model);
    }            
    
	public String makeString() {
		return paren(s1) + " <=> " + paren(s2);
	}

    @Override
    public Sentence toCnf() {        
        Sentence s1 = this.s1.toCnf();
        Sentence s2 = this.s2.toCnf();        
        return new And(new If(s1, s2).toCnf(), new If(s2, s1).toCnf()).toCnf();
    }
}