package csp;


abstract class Arc {
    Variable first;
    Variable second;

    public Arc(Variable var1, Variable var2) {
        first = var1;
        second = var2;
    }

    public String toString() {
        return "(" + first + "," + second + ")";
    }
    
    public abstract boolean allowed(Value v1, Value v2);
    
}