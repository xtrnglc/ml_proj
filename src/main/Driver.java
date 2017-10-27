package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Driver {
	static ArrayList<Model> train = new ArrayList<Model>();
	static ArrayList<Model> test = new ArrayList<Model>();
	static ArrayList<Model> eval = new ArrayList<Model>();
	
	static ArrayList<String> evalIds = new ArrayList<String>();
	static ArrayList<String> trainIds = new ArrayList<String>();
	static ArrayList<String> testIds = new ArrayList<String>();
	public static void main(String args[]) throws FileNotFoundException, IOException {
		
		
		//Parse data.train
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.train"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	Model m = new Model(line);
		    	if(m.labelVal == 0) {
		    		m.labelVal = -1;
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
		
		Perceptron.runPerceptron(train, test, eval);
	}
	
	public static void print(ArrayList<String> predictions) throws IOException {
		String s = "Id,Prediction\n";
		for(int i = 0; i < predictions.size(); i++) {
			s += evalIds.get(i) + "," + predictions.get(i) + "\n";
		}	
		Files.write(Paths.get("./predictions.csv"), s.getBytes());
	}
}
