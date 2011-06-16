package neuralnets;

import java.util.Random;

public class Perceptron {

	double weights[];
	
	public Perceptron(int numInputs) {
		weights = new double[numInputs + 1];
		
		Random rand = new Random();
		for(int i = 0; i < weights.length; i++) {
			weights[i] =  rand.nextFloat();
		}
	}
	
	double g(double x[]) {
		float sum = 0;
		for(int i = 0; i < weights.length; i++) {
			sum += weights[i] * x[i];
		}
		return sum;
	}

	int o(double input[]) {
		if (g(input) > 0)
			return 1;
		else
			return 0;
	}

	public void adjustWeights(double[] sum) {
		for(int i = 0; i < weights.length; i++) {
			weights[i] += sum[i];
		}
	}

}
