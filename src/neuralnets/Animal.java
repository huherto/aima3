/**
 * 
 */
package neuralnets;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;


public class Animal implements Example {
	
	final static int NUM_ATTRS = 17;
	
	String name;
	int attributes[] = new int[Animal.NUM_ATTRS];
	AnimalType type;
	
	public void setType(String typeName) {
		type = AnimalType.valueOf(typeName);
		if (type == null)
			throw new RuntimeException("Unkown type "+typeName);
	}
	
	public boolean isMammal() {
		return type.equals(AnimalType.mammal);
	}
	
	public boolean isAmphibian() {
		return type.equals(AnimalType.amphibian);
	}
	
	public boolean isBird() {
		return type.equals(AnimalType.bird);
	}
	
	public boolean isFish() {
		return type.equals(AnimalType.fish);
	}
	
	public boolean isReptile() {
		return type.equals(AnimalType.reptile);
	}
	
	public boolean isShellfish() {
		return type.equals(AnimalType.shellfish);
	}
	
	@Override
	public int expectedOutput() {
		return isMammal() ? 1 : 0;
	}
	
	public double[] outputVector() {
		
		AnimalType[] atypes = AnimalType.values();
		double vector[] = new double[atypes.length + 1];
		for(int i = 0; i < atypes.length; i++) {			
			if (type.equals(atypes[i])) {
				vector[i + 1] = 1.0;
			}
			else {
				vector[i + 1] = 0.0;
			}
		}
				
		return vector;
	}

	@Override
	public double[] inputVector() {
		double vector[] = new double[Animal.NUM_ATTRS + 1];
		
		vector[0] = 1.0;
		for(int i = 0; i < attributes.length; i++) {
			vector[i + 1] = attributes[i];
		}
		
		return vector;
	}
	
	public static List<Example> readZooData() {
		List<Example> res = new ArrayList<Example>();
		Reader reader = null;
		try {
			reader = new FileReader(new File("zoo.csv"));
			LineIterator iter = IOUtils.lineIterator(reader);
			while(iter.hasNext()) {
				String line = iter.next();
				String fields[] = line.split(",");
				Animal animal = new Animal();
				for(int i = 0; i < fields.length; i++) {
					if (i == 0)
						animal.name = fields[i];
					else if (i == fields.length - 1)
						animal.setType(fields[i]);
					else
						animal.attributes[i - 1] = Integer.parseInt(fields[i]);
				}
				res.add(animal);
			}
		}
		catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		finally {
			IOUtils.closeQuietly(reader);
		}
		return res;
	}
	

}