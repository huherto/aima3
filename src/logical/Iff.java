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
		return s1+"<=>"+s2;
	}
}