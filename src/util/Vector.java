package util;

public class Vector {
	
	float array[];
	
	public Vector(float nums[]) {
		array = nums;
	}
	
	public static Vector sum(Vector a, Vector b) {
		if (a.array.length != b.array.length)
			throw new IllegalMatrixOperation();
		
		float res[] = new float[a.array.length];
		for(int i = 0; i < res.length; i++) {
			res[i] = a.array[i] + b.array[i];
		}
		return new Vector(res);		
	}

	public static Vector dotProduct(Vector a, Vector b) {
		if (a.array.length != b.array.length)
			throw new IllegalMatrixOperation();
		
		float res[] = new float[a.array.length];
		for(int i = 0; i < res.length; i++) {
			res[i] = a.array[i] * b.array[i];
		}
		return new Vector(res);		
	}
	
	public float length() {
		float sum = 0;
		for(int i = 0; i < array.length; i++) {
			sum += array[i] * array[i];
		}
		return sum;
	}

	public int size() {
		return array.length;
	}

	public float at(int pos) {
		return array[pos];
	}
}
