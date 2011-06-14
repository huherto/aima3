package csp;

import junit.framework.TestCase;

public class TestSudoku extends TestCase {
    
    public void test43() {
        
        Sudoku sudoku = new Sudoku();

        BaseBacktrackAlg alg = new OptBacktrackAlg(); 
        
        Assignment assign = new Assignment(sudoku);
        
        assign.setDomain(sudoku.get(0, 1), Sudoku.ONE);
        assign.setDomain(sudoku.get(0, 4), Sudoku.SEVEN);
        assign.setDomain(sudoku.get(0, 5), Sudoku.THREE);
        assign.setDomain(sudoku.get(0, 8), Sudoku.FOUR);
        
        assign.setDomain(sudoku.get(1, 5), Sudoku.SIX);
        assign.setDomain(sudoku.get(1, 6), Sudoku.ONE);
        assign.setDomain(sudoku.get(1, 7), Sudoku.FIVE);
        
        assign.setDomain(sudoku.get(3, 4), Sudoku.SIX);
        assign.setDomain(sudoku.get(3, 6), Sudoku.TWO);

        assign.setDomain(sudoku.get(4, 0), Sudoku.SEVEN);
        assign.setDomain(sudoku.get(4, 1), Sudoku.EIGHT);
        assign.setDomain(sudoku.get(4, 2), Sudoku.ONE);
        assign.setDomain(sudoku.get(4, 7), Sudoku.NINE);

        assign.setDomain(sudoku.get(5, 2), Sudoku.FIVE);
        assign.setDomain(sudoku.get(5, 8), Sudoku.SEVEN);

        assign.setDomain(sudoku.get(6, 5), Sudoku.NINE);
        assign.setDomain(sudoku.get(6, 6), Sudoku.EIGHT);

        assign.setDomain(sudoku.get(7, 0), Sudoku.SIX);
        assign.setDomain(sudoku.get(7, 3), Sudoku.EIGHT);
        assign.setDomain(sudoku.get(7, 6), Sudoku.FIVE);

        assign.setDomain(sudoku.get(8, 2), Sudoku.FOUR);
        assign.setDomain(sudoku.get(8, 7), Sudoku.TWO);
        assign.setDomain(sudoku.get(8, 8), Sudoku.SIX);
        
        sudoku.print(assign);
        
        Assignment result = alg.backtrack(sudoku, assign);
        assertTrue(result.isComplete());
        assertNotNull(alg.findInferences(sudoku, result));
        
        sudoku.print(assign);
        
    }

}
