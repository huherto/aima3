/**
 * 
 */
package logical;


class If extends BinarySentence {
    public If(Sentence s1, Sentence s2) {
        super(s1, s2);
    }

    @Override
    public boolean isTrue(Model model) {
        if (s1.isTrue(model)) {
            return s2.isTrue(model);
        }
        return true;
    }            
    
    public String makeString() {
		return paren(s1) + " => " + paren(s2);
	}

    @Override
    public Sentence toCnf() {
        Sentence s1 = new Not(this.s1).toCnf();
        Sentence s2 = this.s2.toCnf();
        return new Or(s1, s2).toCnf();
    }
}