package csp;

import java.util.ArrayList;
import java.util.List;

public class Sudoku implements ConstraintSatisfactionProblem {
    
    public static final Value ONE   = new Value("1");
    public static final Value TWO   = new Value("2");
    public static final Value THREE = new Value("3");
    public static final Value FOUR  = new Value("4");
    public static final Value FIVE  = new Value("5");
    public static final Value SIX   = new Value("6");
    public static final Value SEVEN = new Value("7");
    public static final Value EIGHT = new Value("8");
    public static final Value NINE  = new Value("9");
    
    // El amor debe ser ...
    public static final Domain SIN_CERO = new Domain();
    static {
        SIN_CERO.add(ONE);
        SIN_CERO.add(TWO);
        SIN_CERO.add(THREE);
        SIN_CERO.add(FOUR);
        SIN_CERO.add(FIVE);
        SIN_CERO.add(SIX);
        SIN_CERO.add(SEVEN);
        SIN_CERO.add(EIGHT);
        SIN_CERO.add(NINE);
    }
    
    static class DiffArc extends Arc {
        
        public DiffArc(Variable var1, Variable var2) {
            super(var1, var2);
        }

        @Override
        public boolean allowed(Value v1, Value v2) {
            return v1 != v2;
        }        
    }
    
    private Variable cells[][];
    
    private List<Variable> vars = new ArrayList<Variable>();
    
    public Sudoku() {
        
        cells = new Variable[9][];
        for(int row = 0; row < 9; row++) {
            cells[row] = new Variable[9];
            for(int col = 0; col < 9; col++) {
                cells[row][col] = new Variable(row+"@"+ col, SIN_CERO);
                vars.add(cells[row][col]);
            }
        }         
    }

    public Variable get(int row, int col) {
        return cells[row][col];
    }
    
    @Override
    public List<Variable> variables() {
        return vars;
    }
    
    List<Arc> rowConstraints(int row, int col) {
        List<Arc> result = new ArrayList<Arc>();
        for(int i = 0; i < 9; i++) {
            if (i != col) {
                Variable var1 = cells[row][col];
                Variable var2 = cells[row][i];
                result.add(new DiffArc(var1, var2));
            }
        }
        
        return result;
    }

    List<Arc> colConstraints(int row, int col) {
        List<Arc> result = new ArrayList<Arc>();
        for(int i = 0; i < 9; i++) {
            if (i != row) {
                Variable var1 = cells[row][col];
                Variable var2 = cells[i][col];
                result.add(new DiffArc(var1, var2));
            }
        }
        
        return result;
    }

    List<Arc> blockConstraints(int row, int col) {
        List<Arc> result = new ArrayList<Arc>();
        int rowOffset = (row / 3) * 3;
        int colOffset = (col / 3) * 3;
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                
                int r = rowOffset + i;
                int c = colOffset + j;
                if (r != row && c != col) {
                    Variable var1 = cells[row][col];
                    Variable var2 = cells[r][c];
                    result.add(new DiffArc(var1, var2));                    
                }
            }
        }
        
        return result;
    }

    @Override
    public List<Arc> allArcs() {
        List<Arc> result = new ArrayList<Arc>();
        
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
                result.addAll(rowConstraints(row, col));
                result.addAll(colConstraints(row, col));
                result.addAll(blockConstraints(row, col));
            }
        }
        
        return result;
    }

    public void print(Assignment assign) {
        for(int row = 0; row < 9; row++) {
            for(int col = 0; col < 9; col++) {
                Variable var = cells[row][col];
                Value val = assign.get(var);
                System.out.print("  "+val);
            }
            System.out.println();
        }
    }

}
