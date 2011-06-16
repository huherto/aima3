package neuralnets;

import java.util.List;

import junit.framework.TestCase;

public class LearningTest extends TestCase {

	public void testPerceptron() throws Exception {
		
		PerceptronTrainer trainer = new PerceptronTrainer();
		
		List<Example> set = Animal.readZooData();
		Perceptron ptron = trainer.train(set);
		
	}
	
	public void testBackPropagation() throws Exception {
		
		BackPropagationNet bpnet= new BackPropagationNet(Animal.NUM_ATTRS, 17, AnimalType.values().length);
		
		List<Example> set = Animal.readZooData();
		
		System.out.println(set.size());
		
		bpnet.trainTimes(100, set);
		
		int good = 0;
		for(Example ex : set) {
		    double o[] = bpnet.calcOutput(ex.inputVector());
		    Animal animal = (Animal)ex;
		    int ord = animal.type.ordinal();
		    
		    int max = 1;
		    for(int i = 2; i < o.length; i++) {
		        if (o[i] > o[max]) {
		            max = i;
		        }
		    }
		    
		    if (ord == max - 1)
		        good++;		    
		}
		
		System.out.println("good="+good);
		
	}
}
