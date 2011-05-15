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
    
    public List<Arc> allArcs() {
        List<Arc> arcs = new LinkedList<Arc>();
        arcs.add(new Arc(WA,  NT));
        arcs.add(new Arc(WA,  SA));
        arcs.add(new Arc(NT,  SA));
        arcs.add(new Arc(NT,  Q));
        arcs.add(new Arc(SA,  Q));
        arcs.add(new Arc(SA,  NSW));
        arcs.add(new Arc(SA,  V));
        arcs.add(new Arc(Q,   NSW));
        arcs.add(new Arc(NSW, V));

        arcs.add(new Arc(NT,  WA));
        arcs.add(new Arc(SA,  WA));
        arcs.add(new Arc(SA,  NT));
        arcs.add(new Arc(Q,   NT));
        arcs.add(new Arc(Q,   SA));
        arcs.add(new Arc(NSW, SA));
        arcs.add(new Arc(V,   SA));
        arcs.add(new Arc(NSW, Q));
        arcs.add(new Arc(V,   NSW));

        return arcs;
    }


}
