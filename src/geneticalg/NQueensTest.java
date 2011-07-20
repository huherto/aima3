package geneticalg;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.junit.Test;


public class NQueensTest {

	Random rand = new Random();
	
	public Solution geneticAlgorithm(Set<Solution> population) {
	
		while(true) {
			Set<Solution> newPopulation = new HashSet<Solution>();
			for(int i = 0; i < population.size(); i++) {
				Solution x = randomSolution(population);
				Solution y = randomSolution(population);
				Solution child = x.reproduce(y);
				if (rand.nextInt(10) == 0) child.mutate();
				newPopulation.add(child);
			}
			population.addAll(newPopulation);
			Solution best = findBest(population);
			if (best.solved())
				return best;
			
		}
	}
	

	private Solution findBest(Set<Solution> population) {
		// TODO Auto-generated method stub
		return null;
	}


	private Solution randomSolution(Set<Solution> population) {
		// TODO Auto-generated method stub
		return null;
	}

	@Test
	public void testSimple() {
		System.out.println("Hola world!");
		Set<Solution> population = new HashSet<Solution>();
		for(int i = 0; i < 10; i++) {
			Solution sol = new NQueenSol();
			sol.randomize();
			population.add(sol);
		}
		geneticAlgorithm(population);
	}
}
