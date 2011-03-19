/**
 * 
 */
package logical;

import java.util.HashMap;
import java.util.Map;


class Model {
    private Map<String, Boolean> values = new HashMap<String, Boolean>();
    
    public Model() {
    }
    
    public void unset(Symbol p) {
    	values.remove(p.name);
	}

	public void set(Symbol p, boolean value) {
		values.put(p.name, value);
	}
	
	public boolean isTrue(String name) {
        return values.get(name);
    }
}