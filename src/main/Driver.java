package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Driver {
	public static void main(String args[]) throws FileNotFoundException, IOException {
		ArrayList<Model> train = new ArrayList<Model>();
		ArrayList<Model> test = new ArrayList<Model>();
		ArrayList<Model> eval = new ArrayList<Model>();
		
		ArrayList<String> evalIds = new ArrayList<String>();
		ArrayList<String> trainIds = new ArrayList<String>();
		ArrayList<String> testIds = new ArrayList<String>();
		
		//Parse data.train
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.train"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	Model m = new Model(line);
		    	train.add(m);
		    }
		}
		
		//parse data.test
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.test"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	Model m = new Model(line);
		    	test.add(m);
		    }
		}
		
		//parse data.eval
		try (BufferedReader br = new BufferedReader(new FileReader("DatasetRetry/data-splits/data.eval"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        // process the line.
		    	Model m = new Model(line);
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
}
