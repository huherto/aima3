package util;

import org.junit.Test;


public class TestMatrix {
    
    @Test
    public void testSum() {
        
        float inita[][] = {
                {3, -1},
                {2, 0}
        };
        float initb[][] = {
                {-7, 2},
                {3, 5}
        };
        float initc[][] = {
                {-4, 1},
                {5, 5}
        };
        Matrix a = new Matrix(inita);
        Matrix b = new Matrix(initb);        
        
        Matrix c = Matrix.sum(a, b);
        assertEquals(c, new Matrix(initc));
    }

    @Test
    public void testSubtract() {
        
        float inita[][] = {
                {3, -1},
                {2, 0}
        };
        float initb[][] = {
                {-7, 2},
                {3, 5}
        };
        float initc[][] = {
                {10, -3},
                {-1, -5}
        };
        Matrix a = new Matrix(inita);
        Matrix b = new Matrix(initb);        
        
        Matrix c = Matrix.subtract(a, b);
        assertEquals(c, new Matrix(initc));
    }
    
    @Test
    public void testMultiply1() {
        
        float inita[][] = {
                {2, -3},
                {7, 5}
        };
        float initb[][] = {
                {10, -8},
                {12, -2}
        };
        float initc[][] = {
                {-16, -10},
                {130, -66}
        };
        Matrix a = new Matrix(inita);
        Matrix b = new Matrix(initb);        
        
        Matrix c = Matrix.multiply(a, b);
        assertEquals(c, new Matrix(initc));
    }
    
    @Test
    public void testMultiply2() {
        
        float inita[][] = {
                {1, 2},
                {3, 4}
        };
        float initb[][] = {
                {5, 6},
                {7, 8}
        };
        float initc[][] = {
                {19, 22},
                {43, 50}
        };
        Matrix a = new Matrix(inita);
        Matrix b = new Matrix(initb);        
        
        Matrix c = Matrix.multiply(a, b);
        assertEquals(c, new Matrix(initc));
    }
    
    @Test
    public void testMultiply3() {
        
        float inita[][] = {
                {3, 1, 2},
                {-2, 0, 5}
        };
        float initb[][] = {
                {-1, 3},
                {0, 5},
                {2, 5}
        };
        float initc[][] = {
                {1, 24},
                {12, 19}
        };
        Matrix a = new Matrix(inita);
        Matrix b = new Matrix(initb);        
        
        Matrix c = Matrix.multiply(a, b);
        assertEquals(c, new Matrix(initc));
    }
    
    @Test
    public void testInverse1() {
        
        float inita[][] = {
                {3, -4},
                {2, -5}
        };
        float initc[][] = {
                {(5f/7), (-4f/7)},
                {(2f/7), (-3f/7)}
        };
        Matrix a = new Matrix(inita);
        
        Matrix c = a.inverse();
        assertEquals(c, new Matrix(initc));
    }
    
    private void assertEquals(Matrix a, Matrix b) {
        if (a.getNumRows() != b.getNumRows())
            throw new AssertionError();
        if (a.getNumCols() != b.getNumCols())
            throw new AssertionError();
        for(int r = 0; r < a.getNumRows(); r++) {
            for(int c = 0; c < b.getNumCols(); c++) {
                float diff = Math.abs(a.at(r, c) - b.at(r, c)); 
                if (diff > 0.0001) {
                    throw new AssertionError();
                }
            }
        }
    }

}
