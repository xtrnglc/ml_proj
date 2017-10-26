package main;

import java.util.HashMap;

public class Model {
	public String label;
	public int labelVal;
	public HashMap<Integer,Double> featureVector;
	
	public Model(String line) {
		featureVector = new HashMap<Integer, Double>();
		String[] parts = line.split(" ");
		
		label = parts[0];
		labelVal = Integer.parseInt(label);
		
		for(int i = 1; i < parts.length; i++) {
			String[] elements = parts[i].split(":");
			featureVector.put(Integer.parseInt(elements[0]), Double.parseDouble(elements[1]));
		}
	}
}