package main;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class BaggedForests {
    static String mostCommonLabel = "+1";
    static int depth = 0;
    static int k = 3;
    static int fvSize = 17;
    static List<Integer> attributes = new ArrayList<>();

    public static void runBaggedForests(List<Model> train, List<Model> test, List<Model> eval) throws IOException {

        for(int i = 1; i < fvSize; i++){
            attributes.add(i);
        }

        int count = 50;

        System.out.println("Forest train");
        List<Node> forestAll = createForest(train, count);
        System.out.println("Train accuracy");
        runTest(forestAll, train);
        System.out.println("Test accuracy");
        runTest(forestAll, test);
        Driver.print(runTest(forestAll, eval));

    }

    public static ArrayList<String> runTest(List<Node> forest, List<Model> test) {
        ArrayList<String> overallPredictions = new ArrayList<String>();
        int success = 0;
        for(Model m : test) {
            List<String> predictions = new ArrayList<String>();
            for(Node n : forest) {
                String prediction = traverseTree(m, n);
                predictions.add(prediction);
            }
            String baggedPrediction = getPrediction(predictions);
            if(baggedPrediction.equals(m.label)){
            	success++;
            }
            if(baggedPrediction.equals("+1")){
                overallPredictions.add("1");
            }
            if(baggedPrediction.equals("0")){
                overallPredictions.add("0");
            }

        }
        System.out.println("Result = " + (double)success / (double)test.size());
        return overallPredictions;
    }

    public static String getPrediction(List<String> predictions) {
        double plusCount = 0;
        double minusCount = 0;

        for(String s : predictions) {
            if(s.equals("+1")){
                plusCount++;
            } else {
                minusCount++;
            }
        }

        if(plusCount > minusCount) {
            return "+1";
        } else {
            return "0";
        }
    }

    public static List<Node> createForest(List<Model> train, int count) {
        List<Node> forest = new ArrayList<Node>();
        for(int i = 0; i < count; i++) {
            System.out.println("Tree " + i);
            Collections.shuffle(train);
            List<Model> subset = train.stream().limit(10000).collect(Collectors.toList());
            Node n = DecisionTree.implementID3(subset, attributes, 1);
            forest.add(n);
        }

        return forest;
    }

    public static String traverseTree(Model m, Node current) {
        //System.out.println(current.value);
        String val;
        if(current.value.equals("0")) {
        	return "0";
        }
        if(current.value.equals("+1")){
            return "+1";
        }
    	
        
        String[] split = current.value.split(":");
        if(m.featureVector.get(Integer.parseInt(split[0])) >= Double.valueOf(split[1])) {
            val = traverseTree(m, current.yesBranch);
        } else {
            val = traverseTree(m, current.noBranch);
        }

        return val;
    }
}
