package util;

import java.util.Arrays;

public class Matrix  {
    
    int numCols;
    
    int numRows;
    
    float array[][];
    
    public Matrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        array = new float[numRows][];
        for(int r = 0; r < array.length; r++) {
            array[r] = new float[numCols];
        }
    }
    
    public Matrix(Matrix other) {
        this.numRows = other.numRows;
        this.numCols = other.numCols;
        array = new float[numRows][];
        for(int r = 0; r < numRows; r++) {
            array[r] = new float[numCols];
            for(int c = 0; c < numCols; c++) {
                array[r][c] = other.array[r][c];
            }
        }
    }
    
    public Matrix(float[][] init) {
        numRows = init.length;
        numCols = init[0].length;
        array = new float[numRows][];
        for(int r = 0; r < numRows; r++) {
            if (init[r].length != numCols)
                throw new IllegalArgumentException();
            array[r] = new float[numCols];
            for(int c = 0; c < numCols; c++) {
                array[r][c] = init[r][c];
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }
    
    public int getNumCols() {
        return numCols;
    }
    
    public Matrix add(Matrix other) {
        if (other.numRows != numRows || other.numCols != numCols)
            throw new IllegalMatrixOperation();
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                array[r][c] += other.array[r][c];
            }
        }
        return this;
    }
    
    public Matrix sub(Matrix other) {
        if (other.numRows != numRows || other.numCols != numCols)
            throw new IllegalMatrixOperation();
        for(int r = 0; r < numRows; r++) {
            for(int c = 0; c < numCols; c++) {
                array[r][c] -= other.array[r][c];
            }
        }
        return this;
    }
    
    public static Matrix sum(Matrix a, Matrix b) {
        Matrix res = new Matrix(a);
        res.add(b);
        return res;
    }
    
    public static Matrix subtract(Matrix a, Matrix b) {
        Matrix res = new Matrix(a);
        res.sub(b);
        return res;
    }
    
    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.numCols != b.numRows)
            throw new IllegalArgumentException();
        
        Matrix res = new Matrix(a.numRows, b.numCols);
        for(int r = 0; r < res.numRows; r++) {
            for(int c = 0; c < res.numCols; c++) {
                res.array[r][c] = sumProduct(r, c, a, b); 
            }
        }
        return res;
    }
    
    private static float sumProduct(int r, int c, Matrix a, Matrix b) {
        float sum = 0;
        for(int i = 0; i < a.numCols; i++) {
            float f1 = a.array[r][i];
            float f2 = b.array[i][c];
            sum += f1 * f2;
        }
        return sum;
    }
    
    public static Matrix LUPSolve(Matrix L, Matrix U, Matrix pi, Matrix b) {
    	int n = L.getNumRows();
    	Matrix y = new Matrix(n, 1);
    	for(int i = 0; i < n; i++) {
    		float sum  = 0;
    		for(int j = 0; j < i; j++) {
    			sum += L.at(i, j) * y.at(j, 0);
    		}
    		float bval = b.at((int)pi.at(i, 0), 0);
    		y.set(i, 0, bval - sum);
    	}
    	System.out.println("y="+y);
    	
    	Matrix x = new Matrix(n, 1);
    	for(int i = n - 1; i >= 0; i--) {
    		float sum  = 0;
    		for(int j = i + 1; j < n; j++) {
    			sum += U.at(i, j) * x.at(j, 0);
    		}
    		float yval = y.at(i, 0);
    		x.set(i, 0, (yval - sum)/U.at(i, i));
    	}
    	System.out.println("x="+x);
    	
    	return x;
    }
    
    public void set(int row, int col, float val) {
    	array[row][col] = val;
	}

	public float at(int r, int c) {
        return array[r][c];
    }

    public Matrix inverse() {
        float a = array[0][0];
        float b = array[0][1];
        float c = array[1][0];
        float d = array[1][1];
        float det = a*d - b*c;
        
        Matrix res = new Matrix(2,2);
        res.array[0][0] = d/det;
        res.array[0][1] = -b/det;
        res.array[1][0] = -c/det;
        res.array[1][1] = a/det;
        return res;
    }
    
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(int r = 0; r < numRows; r++) {
            str.append("{");
            for(int c = 0; c < numCols; c++) {
                str.append(array[r][c]);
                if (c < numCols - 1)
                    str.append(",");
            }
            str.append("}");
            if (r < numRows - 1)
                str.append(",");
        }
        return "Matrix [" + str + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + numCols;
        result = prime * result + numRows;
        result = prime * result + Arrays.hashCode(array);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrix other = (Matrix) obj;
        if (numCols != other.numCols)
            return false;
        if (numRows != other.numRows)
            return false;
        if (!Arrays.equals(array, other.array))
            return false;
        return true;
    }

}
