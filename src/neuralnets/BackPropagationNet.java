package neuralnets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BackPropagationNet {
	
	Random rand = new Random();
	
	int A; // number of units on input layer.
	int B; // number of units on hidden layer.
	int C; // number of units on output layer.

	private double w1[][]; // Weights connecting input layer(i) to hidden layer(j).
	
	private double w2[][]; // Weights connecting hidden layer(i) to output layer(j).

    private double h[]; // hidden units.

    private double o[]; // output units.

	public BackPropagationNet(int numInput, int numHidden, int numOutput) {
		
		A = numInput;
		B = numHidden;
		C = numOutput;
		
		w1 = new double[A + 1][];
		for(int i = 0; i <= A; i++) {
			w1[i] = new double[B + 1];
			for(int j = 1; j <= B; j++) {
				w1[i][j] = random(-0.1, 0.1);
			}
		}
		
		w2 = new double[B + 1][];
		for(int i = 0; i <= B; i++) {
			w2[i] = new double[C + 1];
			for(int j = 1; j <= C; j++) {
				w2[i][j] = random(-0.1, 0.1);
			}
		}
		
        h = new double[B + 1];
        
        o = new double[C + 1];
		
	}

	public void train(double x[], double y[]) {
		
		calcOutput(x);
		
		double delta2[] = new double[C + 1];
		for(int j = 1; j <= C; j++) {
			delta2[j] = o[j] * (1 - o[j]) * (y[j] - o[j]);			
		}
		
		double delta1[] = new double[B + 1];
		for(int j = 1; j <= B; j++) {
			double sum = 0;
			for(int i = 1; i <= C; i++) {
				sum += delta2[i] * w2[j][i];
			}
			delta1[j] = h[j]*(1 - h[j])*sum;
		}
		
		double eta = 0.35; // learning rate.
		
		for(int i = 0; i <= B; i++) {
			for(int j = 1; j <= C; j++) {
				double delta = eta * delta2[j] * h[i];
				w2[i][j] += delta;
			}			
		}
		
		for(int i = 0; i <= A; i++) {
			for(int j = 1; j <= B; j++) {
				double delta = eta * delta1[j] * x[i];
				w1[i][j] += delta;
			}			
		}

	}

    public double[] calcOutput(double[] x) {
        x[0] = 1.0;
		h[0] = 1.0;
		for(int j = 1; j <= B; j++) {
			double sum = 0;
			for(int i = 0; i <= A; i++) {
				sum += w1[i][j] * x[i]; 
			}
			h[j] = 1 / (1 + Math.exp(-sum));				
		}
		
		for(int j = 1; j <= C; j++) {
			double sum = 0;
			for(int i = 0; i <= B; i++) {
				sum += w2[i][j] * h[i];
			}
			o[j] = 1 / (1 + Math.exp(-sum));				
		}
		
		return o;
    }
	
	public void trainEpoch(List<Example> trainSet) {
		
		List<Example> list = new ArrayList<Example>(trainSet);
		
		Collections.shuffle(list);
		
		for(Example pair : list) {
			train(pair.inputVector(), pair.outputVector());
		}		
		
	}
	
	public void trainTimes(int num, List<Example> trainSet) {
		for(int i = 0; i < num; i++) {
			trainEpoch(trainSet);
		}
	}
	
	private double random(double low, double high) {
		double r = rand.nextDouble();		
		double range = high - low;		
		return (r * range) + low;
	}
	
}
