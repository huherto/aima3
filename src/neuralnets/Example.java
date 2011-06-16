/**
 * 
 */
package neuralnets;

public interface Example {
	
	double[] inputVector();
	
	double[] outputVector();

	int expectedOutput();
	
}