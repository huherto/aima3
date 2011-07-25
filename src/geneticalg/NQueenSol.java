package geneticalg;

import java.util.Arrays;
import java.util.Random;

public class NQueenSol implements Solution
{

	static Random rand = new Random();
	
	private int cols[];	
	private int fitness = -1;
		
	public NQueenSol(int numQueens)
	{
		cols = new int[numQueens];
	}

	@Override
	public int fitness()
	{
		if (fitness >= 0)
			return fitness;
		fitness = 1;
		int count = safeQueens();
		for(int i = 0; i < 4; i++) {
		    fitness *= count;
		}
		return fitness;
	}
	
	public int safeQueens() {
        int count = 0;
        for(int i = 0; i < cols.length; i++) 
        {
            if (!hasConflicts(i))
                count++;
        }
        return count;
	}
	
	@Override
	public int hashCode() {
		
		return Arrays.hashCode(cols);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NQueenSol other = (NQueenSol) obj;
		if (!Arrays.equals(cols, other.cols))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NQueenSol [cols=" + Arrays.toString(cols) + "]";
	}

	private boolean hasConflicts(int col)
	{
		
		for(int i = 0; i < cols.length; i++) {
			int dist = Math.abs(col - i);
			if (dist == 0) continue;
			
			if (cols[i] == cols[col]) {
				return true;
			}
			
			if (cols[i] == cols[col] - dist)
				return true;
			
			if (cols[i] == cols[col] + dist)
				return true;
		}
		
		return false;
	}

	@Override
	public void mutate()
	{
		fitness = -1;
		int idx = rand.nextInt(cols.length);
		cols[idx] = rand.nextInt(cols.length);
	}
	
	@Override
	public Solution reproduce(Solution partner)
	{
				
		NQueenSol mother = (NQueenSol)partner;
		
		if (mother.cols.length != cols.length) // different species?
			throw new RuntimeException("Reproduction not allowed");
		
		NQueenSol child = new NQueenSol(cols.length);
		//int idx = 1 + rand.nextInt(cols.length - 2); //para que almenos se conserve un GEN
		int idx = cols.length / 2;
		for(int i = 0; i < cols.length; i++)
		{
			if (i < idx)
				child.cols[i] = this.cols[i];
			else
				child.cols[i] = mother.cols[i];
		}
		return child;
	}

	@Override
	public boolean solved()
	{
		return safeQueens() == cols.length;
	}

	@Override
	public void randomize()
	{
		fitness = -1;
		for(int i = 0; i < cols.length; i++)
		{
			cols[i] = rand.nextInt(cols.length);
		}
	}

	@Override
	public boolean fair() {
		return safeQueens() > cols.length / 2;
	}
}