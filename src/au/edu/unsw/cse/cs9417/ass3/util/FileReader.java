package au.edu.unsw.cse.cs9417.ass3.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import au.edu.unsw.cse.cs9417.ass3.classifier.KNNClassifier;
import au.edu.unsw.cse.cs9417.ass3.classifier.KNNPredictor;
import au.edu.unsw.cse.cs9417.ass3.data.Autos;
import au.edu.unsw.cse.cs9417.ass3.data.Ionosphere;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class FileReader {

	public Enumeration<Instance> readFile(String filePath) {
		DataSource source;
		Instances data = null;
		try {
			source = new DataSource(filePath);
			data = source.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);

		return data.enumerateInstances();
	}

	public Entry<Integer, Double> readForIonosphere(int distanceFunction) {
		String path = "resources/ionosphere.arff";
		List<Ionosphere> itemList = new ArrayList<Ionosphere>();
		Map<Integer, Double> accuracyRate = new HashMap<Integer, Double>();
		// Read and box data
		Enumeration<Instance> instances = this.readFile(path);
		while (instances.hasMoreElements()) {
			itemList.add(new Ionosphere(instances.nextElement()));
		}

		int kValue = 0;
		double accuracy = 0.0;
		// Run leave-one-out validation with different k value, store
		// accuracy rate
		KNNClassifier classifier = new KNNClassifier();
		for (int k = 1; k < itemList.size(); k++) {
			// System.out.print(k + " ");
			double acc = classifier.leaveOneOutValidation(itemList, k,
					distanceFunction);
			if (accuracy < acc) {
				accuracy = acc;
				kValue = k;
			}
			accuracyRate.put(k, acc);
		}
		System.out.println("k=" + kValue + ": " + accuracy);
		// sortAccuracyRate(accuracyRate);
		return accuracyRate.entrySet().iterator().next();
	}

	public Entry<Integer, Double> readForAutos(int distanceFunction,
			double accuracyRange, boolean isWeighted) {
		String path = "resources/autos.arff";
		List<Autos> itemList = new ArrayList<Autos>();
		Map<Integer, Double> accuracyRate = new HashMap<Integer, Double>();
		// Load and prepare data
		Enumeration<Instance> instances = this.readFile(path);
		List<Instance> list = prepareData(instances);
		int id = 0;
		for (Instance i : list) {
			itemList.add(new Autos(i, ++id));
		}

		int kValue = 0;
		double accuracy = 0.0;

		// Run leave-one-out validation with different k value, store
		// accuracy rate
		KNNPredictor predictor = new KNNPredictor();
		for (int k = 1; k < itemList.size(); k++) {
			// System.out.println(k);
			double acc = predictor.leaveOneOutValidation(itemList, k,
					distanceFunction, accuracyRange, isWeighted);
			if (accuracy < acc) {
				accuracy = acc;
				kValue = k;
			}
			accuracyRate.put(k, acc);
		}

		System.out.println("k=" + kValue + ": " + accuracy);
		sortAccuracyRate(accuracyRate);
		return accuracyRate.entrySet().iterator().next();
	}

	// Remove instances with missing values from dataset
	public List<Instance> prepareData(Enumeration<Instance> instances) {
		List<Instance> instanceList = new ArrayList<Instance>();
		while (instances.hasMoreElements()) {
			Instance i = instances.nextElement();
			if (!i.hasMissingValue()) {
				instanceList.add(i);
			}
		}
		return instanceList;
	}

	// Sort accuracyRate from highest to lowest
	public void sortAccuracyRate(Map<Integer, Double> accuracyRate) {
		List<Map.Entry<Integer, Double>> listData = new ArrayList<Map.Entry<Integer, Double>>(
				accuracyRate.entrySet());
		Collections.sort(listData,
				new Comparator<Map.Entry<Integer, Double>>() {
					public int compare(Map.Entry<Integer, Double> o1,
							Map.Entry<Integer, Double> o2) {
						return (int) ((o2.getValue() - o1.getValue()) * 1000);
					}
				});
	}
}
