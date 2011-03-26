package uncertainty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

public class BasicBayesTest extends TestCase {
    
    interface Variable {    
    };
    
    @SuppressWarnings("serial")
    public class Domain extends HashSet<Variable> {
        public Domain() {            
        }
        
        public Domain(Variable vars[]) {
            addAll(Arrays.asList(vars));
        }
    };
    
    enum Cavity implements Variable { cavity, noCavity };
    enum Toothache implements Variable { toothache, noToothache };
    enum Catch implements Variable { katch, noCatch };
    
    double p_OR() {
        return 1;
    }

    @SuppressWarnings("serial")
	class Tuple extends LinkedList<Variable>{
        
        double prob = 0;

        public Tuple() {            
        }
        
        public Tuple(double prob, Variable...vars) {
            this.prob = prob;
            addAll(Arrays.asList(vars));
        }
        
    }
    
    @SuppressWarnings("serial")
    class TupleSet extends HashSet<Tuple> {
        double sumOr(Variable... vars) {
            double sum = 0.0;
            for(Tuple tuple : this) {
                boolean include = false;
                for(Variable var : vars) {
                    if (tuple.contains(var)) {
                        include = true;
                    }
                }
                if (include)
                    sum += tuple.prob;                    
            }
            return sum;
        }
    }
    
    public TupleSet P(Domain...domains) {
        List<Domain> list = new LinkedList<Domain>();
        for(Domain domain: domains) {
            list.add(domain);
        }
        return P(list);
    }
    
    public TupleSet P(List<Domain> list) {
        
        TupleSet result = new TupleSet();
        if (list.isEmpty())
            return result;
        
        Domain firstDomain = list.get(0);
        List<Domain> rest = list.subList(1, list.size());
        
        if (rest.isEmpty()) {
            
            for(Variable var: firstDomain) {
                Tuple tuple = new Tuple();
                tuple.addFirst(var);
                result.add(tuple);
            }
            return result;            
        }
        
        for(Variable var: firstDomain) {
            for(Tuple tuple : P(rest)) {
                tuple.addFirst(var);
                result.add(tuple);
            }
        }
        
        return result;
    }
    
    public TupleSet getFJD() {
        TupleSet fjd = new TupleSet();
        fjd.add(new Tuple(0.108, Cavity.cavity, Toothache.toothache, Catch.katch));
        fjd.add(new Tuple(0.012, Cavity.cavity, Toothache.toothache, Catch.noCatch));
        fjd.add(new Tuple(0.072, Cavity.cavity, Toothache.noToothache, Catch.katch));
        fjd.add(new Tuple(0.008, Cavity.cavity, Toothache.noToothache, Catch.noCatch));
        
        fjd.add(new Tuple(0.016, Cavity.noCavity, Toothache.toothache, Catch.katch));
        fjd.add(new Tuple(0.064, Cavity.noCavity, Toothache.toothache, Catch.noCatch));
        fjd.add(new Tuple(0.144, Cavity.noCavity, Toothache.noToothache, Catch.katch));
        fjd.add(new Tuple(0.576, Cavity.noCavity, Toothache.noToothache, Catch.noCatch));
        return fjd;
    }
    
    public void assertEquals(double d1, double d2) {
        assertTrue(Math.abs(d1 - d2) <= 0.001); 
    }
    
    public void testFullJoinDist() {
        TupleSet fjd = P(new Domain(Cavity.values()), new Domain(Toothache.values()), new Domain(Catch.values()));
        assertEquals(8, fjd.size());
        
        fjd = getFJD();
        assertEquals(8, fjd.size());
        
        assertEquals(fjd.sumOr(Cavity.cavity), 0.2);
        assertEquals(fjd.sumOr(Cavity.cavity, Toothache.toothache), 0.28);
    }
}
