package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Perceptron {

	public static class Tuple {
		public double b;
		public int update;
		
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