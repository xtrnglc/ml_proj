package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Driver {
	static ArrayList<Model> train = new ArrayList<Model>();
	static ArrayList<Model> test = new ArrayList<Model>();
	static ArrayList<Model> eval = new ArrayList<Model>();
	
	static ArrayList<String> evalIds = new ArrayList<String>();
	static ArrayList<String> trainIds = new ArrayList<String>();
	static ArrayList<String> testIds = new ArrayList<String>();
	
	static ArrayList<Integer> features = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16));
	
	public static void main(String args[]) throws FileNotFoundException, IOException {
		parseData();
		//Perceptron.runPerceptron(train, test, eval);
		//Node root = DecisionTree.implementID3(train, features, 1);
		//BaggedForests.runBaggedForests(train, test, eval);
		Perceptron.runBaggedPerceptron(train, test, eval);
		
		System.out.println("Done");
	}
	
	public static void parseData() throws FileNotFoundException, IOException {
		//Parse data.train
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.train"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	Model m = new Model(line);
		    	if(m.labelVal == 0) {
		    		m.labelVal = -1;
		    	}
		    	if(m.labelVal == 1) {
		    		m.label = "+1";
		    	}
		    	train.add(m);
		    }
		}
		
		//parse data.test
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.test"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	Model m = new Model(line);
		    	if(m.labelVal == 0) {
		    		m.labelVal = -1;
		    	}
		    	if(m.labelVal == 1) {
		    		m.label = "+1";
		    	}
		    	test.add(m);
		    }
		}
		
		//parse data.eval
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.eval.anon"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	Model m = new Model(line);
		    	if(m.labelVal == 0) {
		    		m.labelVal = -1;
		    	}
		    	if(m.labelVal == 1) {
		    		m.label = "+1";
		    	}
		    	eval.add(m);
		    }
		}
		
		//parse data.train.id
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.train.id"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	trainIds.add(line);
		    }
		}
		
		//parse data.test.id
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.test.id"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	testIds.add(line);
		    }
		}
				
		//parse data.eval.id
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.eval.id"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	evalIds.add(line);
		    }
		}
	}
	
	public static void print(ArrayList<String> predictions) throws IOException {
		String s = "Id,Prediction\n";
		for(int i = 0; i < predictions.size(); i++) {
			s += evalIds.get(i) + "," + predictions.get(i) + "\n";
		}	
		Files.write(Paths.get("./predictions.csv"), s.getBytes());
	}
}
