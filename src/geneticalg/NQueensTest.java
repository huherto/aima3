package geneticalg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;


public class NQueensTest {

	Random rand = new Random();
	
	public Solution geneticAlgorithm(Set<Solution> population) {
	
		while(true) {
			Set<Solution> newPopulation = new HashSet<Solution>();
			System.out.println("population="+population.size());
			for(int i = 0; i < population.size(); i++) {
				Solution x = randomSolution(population);
				Solution y = randomSolution(population);
				Solution child = x.reproduce(y);
				if (rand.nextInt(20) == 0) child.mutate();
				if (child.fair())
					newPopulation.add(child);
			}
			population.addAll(newPopulation);
			fitnessStats(population);
			Solution best = findBest(population);
			if (best.solved() || population.size() >= 30000)
				return best;
			
		}
	}
	

	private static class Counter {
		public int val = 0;
	};
	
	private void fitnessStats(Set<Solution> population) {
		
		Map<Integer, Counter> map = new HashMap<Integer, Counter>();
		for(Solution sol:population) {
			int fitness = sol.fitness();
			if (!map.containsKey(fitness))
				map.put(fitness, new Counter());
			map.get(fitness).val++;
		}
		
		List<Integer> ordered = new ArrayList<Integer>(map.keySet());
		Collections.sort(ordered);
		
		for(Integer fitness : ordered) {
			System.out.println(fitness+"\t"+map.get(fitness).val);
		}
		
	}


	private Solution findBest(Set<Solution> population)
	{
		Solution best = null;
		for(Solution sol:population)
		{
			if(best == null || sol.fitness() > best.fitness())
			{
				best = sol;
			}
		}
		return best;
	}


	private Solution randomSolution(Set<Solution> population) 
	{
		int sum  = 0;
		for(Solution sol:population)
		{
			sum += sol.fitness() * sol.fitness() * sol.fitness();
		}
		int acum = 0;
		int aux = rand.nextInt(sum);
		Solution last= null;
		for(Solution sol:population)
		{
			if(aux <= acum){
				return sol;
			}
			acum += sol.fitness() * sol.fitness() * sol.fitness();
			last = sol;
		}
		return last;
	}

	@Test
	public void testSimple() {
		Set<Solution> population = new HashSet<Solution>();
		while(population.size() < 100) {
			Solution sol = new NQueenSol(12);
			sol.randomize();
			if (sol.fair())
				population.add(sol);
		}
		Solution res = geneticAlgorithm(population);
		
		System.out.println(res.toString());
	}
}
