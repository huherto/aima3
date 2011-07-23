package geneticalg;

public interface Solution {

	int fitness();

	void mutate();

	boolean solved();

	void randomize();
	
	Solution reproduce(Solution partner);

	boolean fair();

}