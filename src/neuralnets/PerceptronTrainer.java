package neuralnets;

import java.util.ArrayList;
import java.util.List;

public class PerceptronTrainer {

	
	public Perceptron train(List<Example> trainingSet) {
		
		int NUM_ATTRS = trainingSet.get(0).inputVector().length - 1;
		
		Perceptron ptron = new Perceptron(NUM_ATTRS);
		
		while(true) {
			
			List<Example> missed = new ArrayList<Example>();
			for(Example ex : trainingSet) {
				
				int output = ptron.o(ex.inputVector());
				
				if (output != ex.expectedOutput()) {
					missed.add(ex);
				}
			}
			System.out.println("missed "+missed.size());
			if (missed.isEmpty())
				break;
			
			double sum[] = new double[NUM_ATTRS + 1];
			for(int i = 0; i < sum.length; i++) {
				sum[i] = 0;
			}
			
			for(Example ex: missed) {
				
				double[] vector = ex.inputVector();
				int output = ptron.o(vector);
				int sign;
				if (output == 0 && ex.expectedOutput() == 1) {
					sign = 1;
				}
				else {
					sign = -1;
				}
				
				for(int i = 0; i < sum.length; i++) {
					sum[i] += sign * vector[i]; 
				}				
			}
			
			float factor = (float)0.5;
			for(int i = 0; i < sum.length; i++) {
				sum[i] *= factor;
			}
			
			ptron.adjustWeights(sum);
		}

		return ptron;
	}
	
}
