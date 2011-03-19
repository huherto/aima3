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
		return s1+"=>"+s2;
	}
}