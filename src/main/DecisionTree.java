package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DecisionTree {
//	public static double bestInfoGain = Double.MIN_VALUE;
//	public static int bestAttribute = 0;
//	public static double bestX = Double.MIN_VALUE;
	static String mostCommonLabel = "0";
	static int m = 4;
	
	static double[] splits = {9, 75, 42, 19, 52, 33, 237, 293, 1.0, 190, 2, .33, .326, .069, .0197, 0.306};
	
	public static double getEntropy(List<Model> learningSet) {
		double positiveCount = 0;
		double negativeCount = 0;
		double total = learningSet.size();
		
		for(Model m : learningSet) {
			if(m.label.equals("+1")){
				positiveCount++;
			} else {
				negativeCount++;
			}
		}
		
		double val =  (-(positiveCount / total) * (Math.log(positiveCount / total) / Math.log(2))) - ((negativeCount / total) * (Math.log(negativeCount / total) / Math.log(2)));
		return val;
	}
	
	public static double getInformationGain(double overallEntropy, double yesEntropy, double noEntropy, double yesTotal, double noTotal) {
		double total = yesTotal + noTotal;
		if(Double.isNaN(yesEntropy)) {
			yesEntropy = 0;
		}
		if(Double.isNaN(noEntropy)) {
			noEntropy = 0;
		}
		return overallEntropy - ((yesTotal / total) * yesEntropy + (noTotal / total) * noEntropy);
	}
	
	public static double getXThatSplitsA(List<Model> learningSet, int attribute) {
		double x = 0;
		double prevInformationGain = Double.MIN_VALUE;
		
		Collections.sort(learningSet, new Comparator<Model>(){
		     public int compare(Model o1, Model o2){
		    	 if (o1.featureVector.get(attribute) < o2.featureVector.get(attribute) )
		    		 return -1;
		    	 else if (o1.featureVector.get(attribute) > o2.featureVector.get(attribute))
	    		     return 1;
	    		 else
	    		     return 0;
		     }
		});
		double avg = 0;
//		for(int i = 0; i < learningSet.size()-1; i++) {
//			//System.out.println(i);
//			avg += learningSet.get(i).featureVector.get(attribute);
//		}
//		avg = avg / learningSet.size();
		
		for(int i = 0; i < learningSet.size()-1; i++) {
			//System.out.println(i);
			Model m1 = learningSet.get(i);		
			Model m2 = learningSet.get(i+1);
			double a = (m1.featureVector.get(attribute) + m2.featureVector.get(attribute)) /2;
			
			List<Model> lessSubset = getLessSubset(learningSet, attribute, a);
			List<Model> moreSubset = getMoreSubset(learningSet, attribute, a);
			
			double overallEntropy = getEntropy(learningSet);
			double lessEntropy = getEntropy(lessSubset);
			double moreEntropy = getEntropy(moreSubset);
			double informationGain = getInformationGain(overallEntropy, lessEntropy, moreEntropy, lessSubset.size(), moreSubset.size());	
			
			if(informationGain >= prevInformationGain) {
				x = a;
				prevInformationGain = informationGain;
			}	
		}
		
		//x = avg;
		
		return x;
	}
	
	public static List<Model> getLessSubset(List<Model> learningSet, int attribute, double x) {
		List<Model> lessSubset = new ArrayList<Model>();
		
		for(Model m : learningSet) {
			if(m.featureVector.containsKey(attribute)) {
				if(m.featureVector.get(attribute) < x) {
					lessSubset.add(m);
				}
			}
		}
		return lessSubset;
	}
	
	public static List<Model> getMoreSubset(List<Model> learningSet, int attribute, double x) {
		List<Model> moreSubset = new ArrayList<Model>();
		
		for(Model m : learningSet) {
			if(m.featureVector.containsKey(attribute)) {
				if(m.featureVector.get(attribute) >= x) {
					moreSubset.add(m);
				}
			}
		}
		return moreSubset;
	}

	public static String getMostCommonLabel(List<Model> learningSet) {
		double positiveCount = 0;
		double negativeCount = 0;
		
		for(Model m : learningSet) {
			if(m.label.equals("+1")){
				positiveCount++;
			} else {
				negativeCount++;
			}
		}
		
		if(positiveCount > negativeCount) {
			return "+1";
		} else {
			return "0";
		}
	}
	
	public static boolean checkForSameLabels(List<Model> learningSet) {
		String label = learningSet.get(0).label;
		
		for(int i = 1; i < learningSet.size(); i++) {
			String currentLabel = learningSet.get(i).label;
			if(label != currentLabel) {
				return false;
			}
		}
		
		return true;
	}

	public static Node implementID3_RF(List<Model> learningSet, List<Integer> Attributes, int depth) {
		 //If all examples have same label
        Node root = null;

        if(checkForSameLabels(learningSet) || Attributes.isEmpty()){
            root = new Node();
            root.value = getMostCommonLabel(learningSet);
            //System.out.println(depth);
            return root;
        } else{
            double prevInformationGain = -1;
            int attributeToSplit = 0;
            double informationGain = Double.MIN_VALUE;
            double attributeAvg = 0;
            
            List<Integer> attributesSubset = Attributes.stream().limit(m).collect(Collectors.toList());

            
            for (int a : attributesSubset) {
            	//double avg = splits[a-1];
            	double avg = getXThatSplitsA(learningSet, a);
                List<Model> yesSubset = getMoreSubset(learningSet, a, avg);
                List<Model> noSubset = getLessSubset(learningSet, a, avg);
                double overallEntropy = getEntropy(learningSet);
                double yesEntropy = getEntropy(yesSubset);
                double noEntropy = getEntropy(noSubset);
                informationGain = getInformationGain(overallEntropy, yesEntropy, noEntropy, yesSubset.size(), noSubset.size());
                if(Double.isNaN(informationGain)) {
                    informationGain = 0;
                }
                //System.out.println("Information gain for " + Attributes.get(a) + " " + informationGain);
                if(informationGain >= prevInformationGain) {
                    attributeToSplit = a;
                    attributeAvg = avg;
                    prevInformationGain = informationGain;
                }
            }

            List<Model> yesSubset = getMoreSubset(learningSet, attributeToSplit, attributeAvg);
            List<Model> noSubset = getLessSubset(learningSet, attributeToSplit, attributeAvg);

            root = new Node();
            if(attributeToSplit == 0) {
                System.out.print("");
            }
            root.value = String.valueOf(attributeToSplit + ":" + attributeAvg);

            
            List<Integer> newAttributes = new ArrayList<Integer>();

            for(int i : Attributes){
                if(i != attributeToSplit){
                    newAttributes.add(i);
                }
            }

            if(yesSubset.isEmpty()) {
                Node labelNode = new Node();
                labelNode.value = mostCommonLabel;
                root.yesBranch = labelNode;
            } else {
                Node node = implementID3_RF(yesSubset, newAttributes, depth+1);
                root.yesBranch = node;
            }
            if(noSubset.isEmpty()) {
                Node labelNode = new Node();
                labelNode.value = mostCommonLabel;
                root.noBranch = labelNode;
            } else {
                Node node = implementID3_RF(noSubset, newAttributes, depth+1);
                root.noBranch = node;
            }
       
        }
        root.yesBranchVal = root.yesBranch.value;
        root.noBranchVal = root.noBranch.value;

        if(root.value != "+1" && root.value != "-1"){
            //System.out.println(depth);
        }

        return root;
	}
	
	public static Node implementID3(List<Model> learningSet, List<Integer> Attributes, int depth) {
        //If all examples have same label
        Node root = null;

        if(checkForSameLabels(learningSet) || Attributes.isEmpty()){
            root = new Node();
            root.value = getMostCommonLabel(learningSet);
            //System.out.println(depth);
            return root;
        } else{
            double prevInformationGain = -1;
            int attributeToSplit = 0;
            double informationGain = Double.MIN_VALUE;
            double attributeAvg = 0;
            for (int a : Attributes) {
            	//double avg = splits[a-1];
            	double avg = getXThatSplitsA(learningSet, a);
                List<Model> yesSubset = getMoreSubset(learningSet, a, avg);
                List<Model> noSubset = getLessSubset(learningSet, a, avg);
                double overallEntropy = getEntropy(learningSet);
                double yesEntropy = getEntropy(yesSubset);
                double noEntropy = getEntropy(noSubset);
                informationGain = getInformationGain(overallEntropy, yesEntropy, noEntropy, yesSubset.size(), noSubset.size());
                if(Double.isNaN(informationGain)) {
                    informationGain = 0;
                }
                //System.out.println("Information gain for " + Attributes.get(a) + " " + informationGain);
                if(informationGain >= prevInformationGain) {
                    attributeToSplit = a;
                    attributeAvg = avg;
                    prevInformationGain = informationGain;
                }
            }

            List<Model> yesSubset = getMoreSubset(learningSet, attributeToSplit, attributeAvg);
            List<Model> noSubset = getLessSubset(learningSet, attributeToSplit, attributeAvg);

            root = new Node();
            if(attributeToSplit == 0) {
                System.out.print("");
            }
            root.value = String.valueOf(attributeToSplit + ":" + attributeAvg);

            if(depth < 17) {
                List<Integer> newAttributes = new ArrayList<Integer>();

                for(int i : Attributes){
                    if(i != attributeToSplit){
                        newAttributes.add(i);
                    }
                }

                if(yesSubset.isEmpty()) {
                    Node labelNode = new Node();
                    labelNode.value = mostCommonLabel;
                    root.yesBranch = labelNode;
                } else {
                    Node node = implementID3(yesSubset, newAttributes, depth+1);
                    root.yesBranch = node;
                }
                if(noSubset.isEmpty()) {
                    Node labelNode = new Node();
                    labelNode.value = mostCommonLabel;
                    root.noBranch = labelNode;
                } else {
                    Node node = implementID3(noSubset, newAttributes, depth+1);
                    root.noBranch = node;
                }
            } else{
                Node yesNode = new Node();
                yesNode.value = getMostCommonLabel(yesSubset);
                root.yesBranch = yesNode;

                Node noNode = new Node();
                noNode.value = getMostCommonLabel(noSubset);
                root.noBranch = noNode;
            }
        }
        root.yesBranchVal = root.yesBranch.value;
        root.noBranchVal = root.noBranch.value;

        if(root.value != "+1" && root.value != "-1"){
            //System.out.println(depth);
        }

        return root;
    }
	
		}
