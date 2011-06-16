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
		
		BackPropagationNet bpnet= new BackPropagationNet(Animal.NUM_ATTRS, 10, AnimalType.values().length);
		
		List<Example> set = Animal.readZooData();
		
		bpnet.trainTimes(10, set);
		
	}
}
