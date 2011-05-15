package csp;


class Arc {
    Variable first;
    Variable second;

    public Arc(Variable p1, Variable p2) {
        first = p1;
        second = p2;
    }

    public String toString() {
        return "(" + first + "," + second + ")";
    }
}