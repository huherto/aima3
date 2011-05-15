package csp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapColoring implements ConstraintSatisfactionProblem {    

    public static final Value RED   = new Value("Red");
    public static final Value BLUE  = new Value("Blue");
    public static final Value GREEN = new Value("Green");
    public static Domain allColors = new Domain();

    static {
        allColors.add(RED);
        allColors.add(BLUE);
        allColors.add(GREEN);
    }
    
    private List<Variable> vars = new ArrayList<Variable>();
    
    MapColoring() {
        vars.add(WA);
        vars.add(NT);
        vars.add(Q);
        vars.add(NSW);
        vars.add(V);
        vars.add(SA);
        vars.add(T);
    }
    
    @Override
    public List<Variable> variables() {
        return vars;
    }
    
    public static final Variable WA  = new Variable("WA", allColors);
    public static final Variable NT  = new Variable("NT", allColors);
    public static final Variable Q   = new Variable("Q", allColors);
    public static final Variable NSW = new Variable("NSW", allColors);
    public static final Variable V   = new Variable("V", allColors);
    public static final Variable SA  = new Variable("SA", allColors);
    public static final Variable T   = new Variable("T", allColors);
    
    class DiffColorArc extends Arc {

        public DiffColorArc(Variable var1, Variable var2) {
            super(var1, var2);
        }

        @Override
        public boolean allowed(Value var1, Value var2) {
            return var1 != var2;
        }
        
    }
    
    public List<Arc> allArcs() {
        List<Arc> arcs = new LinkedList<Arc>();
        arcs.add(new DiffColorArc(WA,  NT));
        arcs.add(new DiffColorArc(WA,  SA));
        arcs.add(new DiffColorArc(NT,  SA));
        arcs.add(new DiffColorArc(NT,  Q));
        arcs.add(new DiffColorArc(SA,  Q));
        arcs.add(new DiffColorArc(SA,  NSW));
        arcs.add(new DiffColorArc(SA,  V));
        arcs.add(new DiffColorArc(Q,   NSW));
        arcs.add(new DiffColorArc(NSW, V));

        arcs.add(new DiffColorArc(NT,  WA));
        arcs.add(new DiffColorArc(SA,  WA));
        arcs.add(new DiffColorArc(SA,  NT));
        arcs.add(new DiffColorArc(Q,   NT));
        arcs.add(new DiffColorArc(Q,   SA));
        arcs.add(new DiffColorArc(NSW, SA));
        arcs.add(new DiffColorArc(V,   SA));
        arcs.add(new DiffColorArc(NSW, Q));
        arcs.add(new DiffColorArc(V,   NSW));

        return arcs;
    }

}
