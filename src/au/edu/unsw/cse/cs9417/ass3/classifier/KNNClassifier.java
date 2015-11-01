package au.edu.unsw.cse.cs9417.ass3.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.unsw.cse.cs9417.ass3.data.Ionosphere;

public class KNNClassifier {

	public Ionosphere KNNClassification(List<Ionosphere> data,
			Ionosphere instance, int k, int distanceFunction) {
		Map<Ionosphere, Double> distanceMap = new HashMap<Ionosphere, Double>();
		List<Ionosphere> kNeighbour = new ArrayList<Ionosphere>();

		for (Ionosphere ionosphere : data) {
			switch (distanceFunction) {
			case 1:
				distanceMap.put(ionosphere, euclideanDistance(ionosphere.getList(),
								instance.getList()));
				break;
			case 2:
				distanceMap.put(ionosphere, ManhattanDistance(ionosphere.getList(),
								instance.getList()));
				break;
			case 3:
				distanceMap.put(ionosphere, CanDistance(ionosphere.getList(), instance.getList()));
				break;
			}
		}

		List<Map.Entry<Ionosphere, Double>> listData = new ArrayList<Map.Entry<Ionosphere, Double>>(
				distanceMap.entrySet());

		Collections.sort(listData,
				new Comparator<Map.Entry<Ionosphere, Double>>() {
					public int compare(Map.Entry<Ionosphere, Double> o1,
							Map.Entry<Ionosphere, Double> o2) {
						return (int) ((o1.getValue() - o2.getValue()) * 1000);
					}
				});

		for (int i = 0; i < k; i++) {
			kNeighbour.add(listData.get(i).getKey());
		}

		instance.setPredictClassification(commonClassification(kNeighbour));

		return instance;
	}

	public String commonClassification(List<Ionosphere> neighbours) {
		int counter = 0;
		for (Ionosphere neighbour : neighbours) {
			if (neighbour.getActualClassification().equals("g"))
				counter++;
		}

		if (counter * 2 - neighbours.size() >= 0)
			return "g";
		else
			return "b";
	}

	public double accuracyCalculator1(List<Ionosphere> dataList) {
		int accurateInstances = 0;

		for (Ionosphere ionosphere : dataList) {
			if (ionosphere.getActualClassification().equalsIgnoreCase(
					ionosphere.getPredictClassification())) {
				accurateInstances++;
			}
		}
		return (double) accurateInstances / dataList.size();
	}

	public double leaveOneOutValidation(List<Ionosphere> dataSet, int k,
			int distanceFunction) {
		List<Ionosphere> list = new ArrayList<Ionosphere>();
		for (int i = 0; i < dataSet.size(); i++) {
			Ionosphere instance = dataSet.get(0);
			dataSet.remove(instance);
			instance.setPredictClassification(" ");
			list.add(KNNClassification(dataSet, instance, k, distanceFunction));
			dataSet.add(instance);
		}
		double accuracyRate = accuracyCalculator1(list);
		cleanUpPredictValue(dataSet);
//		System.out.println(accuracyRate);
		return accuracyRate;
	}

	private void cleanUpPredictValue(List<Ionosphere> dataSet) {
		for (Ionosphere ionosphere : dataSet)
			ionosphere.setPredictClassification(null);
	}

	/**
	 * Distance functions
	 */
	// Distance function for numerical attribute
	public double euclideanDistance(List<Double> data1, List<Double> data2) {
		// System.out.println("calculating distance...");
		double distance = 0;
		if (data1.size() == data2.size()) {
			for (int i = 0; i < data1.size() - 1; i++) {
				distance += Math.pow(data1.get(i) - data2.get(i), 2);
			}
		}
		return Math.sqrt(distance);
	}

	// Manhattan Distance Measure
	public double ManhattanDistance(List<Double> data1, List<Double> data2) {
		double distance = 0;
		if (data1.size() == data2.size()) {
			for (int i = 0; i < data1.size() - 1; i++) {
				distance += Math.abs((data1.get(i) - data2.get(i)));
			}
		}
		return distance;
	}

	// Canberra_distance
	public double CanDistance(List<Double> data1, List<Double> data2) {
		double distance = 0;
		if (data1.size() == data2.size()) {
			for (int i = 0; i < data1.size() - 1; i++) {
				distance += Math.abs((data1.get(i) - data2.get(i)))
						/ (Math.abs(data1.get(i)) + Math.abs(data2.get(i)));
			}
		}
		return distance;
	}
}
