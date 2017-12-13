package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



public class Perceptron {

	static int[] attributes = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
	
	public static class Tuple {
		public double b;
		public double[] w;
	}
	
	public static void runBaggedPerceptron(ArrayList<Model> train, ArrayList<Model> test, ArrayList<Model> eval) {
		List<Tuple> forest = createBagged(train, 10);
        ArrayList<String> overallPredictions = new ArrayList<String>();


		int successTrain = 0;
		//test on train
		for(Model m : train) {
	        ArrayList<String> predictions = new ArrayList<String>();

			for(Tuple t : forest) {
				int prediction = perceptronTest(m,t.w,t.b);
				String p = "";
				if(prediction > 0) {
					predictions.add("+1");
				} else {
					predictions.add("0");
				}
			}
			String baggedPrediction = getPrediction(predictions);
			if(baggedPrediction.equals(m.label)){
				successTrain++;
            }
            if(baggedPrediction.equals("+1")){
                overallPredictions.add("1");
            }
            if(baggedPrediction.equals("0")){
                overallPredictions.add("0");
            }
		}
		System.out.println("Result = " + (double)successTrain / (double)train.size());
	}
	
	public static String getPrediction(List<String> predictions) {
        double plusCount = 0;
        double minusCount = 0;

        for(String s : predictions) {
            if(s.equals("1")){
                plusCount++;
            } else {
                minusCount++;
            }
        }

        if(plusCount > minusCount) {
            return "1";
        } else {
            return "0";
        }
    }
	
	 public static List<Tuple> createBagged(List<Model> train, int count) {
	        List<Tuple> forest = new ArrayList<Tuple>();
	        for(int i = 0; i < count; i++) {
	            System.out.println("Perceptron " + i);
	            Collections.shuffle(train);
	            List<Model> subset = train.stream().limit(10000).collect(Collectors.toList());
	            Tuple t = runPerceptron(subset);
	            forest.add(t);
	        }

	        return forest;
	    }
	 
	 public static Tuple runPerceptron(List<Model> train) {
			double[] w = {0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01};
			double b = 0.001;
			double mu = 0.01;
			
			for(Model m : train) {
				b = simplePerceptronTrain(m, 10, w, b, mu);
			}
			
			Tuple t = new Tuple();
			t.b = b;
			t.w = w;
			
			return t;
	 }
	
	public static void runPerceptron(ArrayList<Model> train, ArrayList<Model> test, ArrayList<Model> eval) throws FileNotFoundException, IOException {
		double[] w = {0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01,0.01};
		double b = 0.001;
		double learningRate = 0.1;
		double mu = 0.01;
		
		for(Model m : train) {
			b = simplePerceptronTrain(m, 10, w, b, mu);
		}
		
		ArrayList<String> predictions = new ArrayList<String>();
		
		System.out.println("train accuracy");
		int successTrain = 0;
		for(Model m : train) {
			int predicted =  perceptronTest(m, w, b);
			String p = "";
			if(predicted > 0) {
				//predictions.add("1");
				p = "+1";
			} else {
				//predictions.add("0");
				p = "0";
			}
			if(m.label.equals(p)) {
				successTrain++;
			}
		}
		System.out.println("Result = " + (double)successTrain / (double)train.size());
		
		int successTest = 0;
		for(Model m : test) {
			int predicted =  perceptronTest(m, w, b);
			String p = "";
			if(predicted > 0) {
				//predictions.add("1");
				p = "+1";
			} else {
				//predictions.add("0");
				p = "0";
			}
			if(m.label.equals(p)) {
				successTest++;
			}
		}
		System.out.println("Result = " + (double)successTest / (double)test.size());
		
		for(Model m : eval){
			int predicted =  perceptronTest(m, w, b);
			if(predicted > 0) {
				predictions.add("1");
			} else {
				predictions.add("0");
			}
		}
		
		Driver.print(predictions);
	}
	
	public static double simplePerceptronTrain(Model data, int maxIter, double[] w, double b, double learningRate) {
		double sum = 0;
		
		for(int i = 0; i < maxIter; i++) {
			for(int j = 0; j < w.length; j++) {
				if(data.featureVector.containsKey(j)){
					sum += w[j] * data.featureVector.get(j);
				}
			}
			sum += b;
			
			if(sum * data.labelVal <= 0) {
				for(int j = 0; j < w.length; j++) {
					if(data.featureVector.containsKey(j)){
						w[j] += learningRate * data.labelVal * data.featureVector.get(j);
					}
				}
				b += data.labelVal;
			}
		}
		return b;
	}
	
	public static double marginPerceptronTrain(Model data, int maxIter, double[] w, double b, double learningRate, double mu) {
		double sum = 0;
		double dynamicLearningRate = learningRate;
		int timeStep = 0;
		for(int i = 0; i < maxIter; i++) {
			for(int j = 0; j < w.length; j++) {
				if(data.featureVector.containsKey(j)){
					sum += w[j] * data.featureVector.get(j);
				}
			}
			sum += b;
			
			if(sum * data.labelVal < mu) {
				for(int j = 0; j < w.length; j++) {
					if(data.featureVector.containsKey(j)){
						w[j] += dynamicLearningRate * data.labelVal * data.featureVector.get(j);
					}
				}
				b += data.labelVal;
			}
			timeStep++;
			dynamicLearningRate = learningRate / (1 + timeStep);
		}
		return b;
	}
	
	public static int perceptronTest(Model data, double[] w, double b) {
		double sum = 0;
		for(int j = 0; j < w.length; j++) {
			if(data.featureVector.containsKey(j)){
				sum += w[j] * data.featureVector.get(j);
			}
		}
		
		return (int)Math.signum(sum);
		
	}
}