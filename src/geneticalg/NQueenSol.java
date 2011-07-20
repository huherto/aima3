package geneticalg;

import java.util.Random;

public class NQueenSol implements Solution {

	static Random rand = new Random();
	
	private static final int NUM_QUEENS = 8;
	private int cols[];		
		
	public NQueenSol() {
		cols = new int[NUM_QUEENS];
	}

	@Override
	public int fitness() {
		int count = 0;
		for(int i = 0; i < cols.length; i++) {
			if (hasConflicts(i))
				count++;
		}
		return NUM_QUEENS - count;
	}
	
	private boolean hasConflicts(int col) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void mutate() {
		int idx = rand.nextInt(NUM_QUEENS);
		cols[idx] = rand.nextInt(NUM_QUEENS);
	}
	
	@Override
	public Solution reproduce(Solution partner) {
		NQueenSol mother = (NQueenSol)partner;
		NQueenSol child = new NQueenSol();
//		int idx = NUM_QUEENS / 2;
		int idx = rand.nextInt(NUM_QUEENS);
		for(int i = 0; i < cols.length; i++) {
			if (i < idx)
				child.cols[i] = this.cols[i];
			else
				child.cols[i] = mother.cols[i];
		}
		return child;
	}

	@Override
	public boolean solved() {
		return fitness() == NUM_QUEENS;
	}

	@Override
	public void randomize() {
		for(int i = 0; i < cols.length; i++) {
			cols[i] = rand.nextInt(NUM_QUEENS);
		}
	}
}