package au.edu.unsw.cse.cs9417.ass3.classifier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.unsw.cse.cs9417.ass3.data.Autos;

public class KNNPredictor {

	public KNNPredictor() {
	}

	public double leaveOneOutValidation(List<Autos> dataSet, int k,
			int distanceFunction, double accuracyRange, boolean isWeighted) {
		List<Autos> list = new ArrayList<Autos>();
		for (int i = 0; i < dataSet.size(); i++) {
			Autos instance = dataSet.get(0);
			dataSet.remove(instance);
			list.add(KNNNumericPrediction(dataSet, instance, k,
					distanceFunction, isWeighted));
			dataSet.add(instance);
		}
		double accuracyRate = accuracyCalculator(list, accuracyRange);
//		System.out.println(accuracyRate);
		cleanUpPredictValue(dataSet);
		return accuracyRate;
	}

	public Autos KNNNumericPrediction(List<Autos> data, Autos instance, int k,
			int distanceFunction, boolean isWeighted) {
		List<Autos> kNeighbour = new ArrayList<Autos>();
		Map<Autos, Double> distanceMap = new HashMap<Autos, Double>();
		
		// calculate distance for each instance in dataset with instance
		for (Autos auto : data) {
			switch (distanceFunction) {
			case 1:
				distanceMap.put(auto, euclideanDistance(auto.getNumericList(),
								instance.getNumericList()));
				break;
			case 2:
				distanceMap.put(auto, ManhattanDistance(auto.getNumericList(),
								instance.getNumericList()));
				break;
			case 3:
				distanceMap.put(auto, CanDistance(auto.getNumericList(),
								instance.getNumericList()));
				break;
			}
		}
		
		// Sort dataset according to distance
		List<Map.Entry<Autos, Double>> listData = new ArrayList<Map.Entry<Autos, Double>>(
				distanceMap.entrySet());
		Collections.sort(listData, new Comparator<Map.Entry<Autos, Double>>() {
			public int compare(Map.Entry<Autos, Double> o1,
					Map.Entry<Autos, Double> o2) {
				return (int) ((o1.getValue() - o2.getValue()) * 1000);
			}
		});

		for (int i = 0; i < k; i++) {
			kNeighbour.add(listData.get(i).getKey());
		}
		
		// calculate predict value based on nearest k instances
		if (!isWeighted) {
			instance.setPredictPrice(calculatePrediction(kNeighbour));
		} else {
			instance.setPredictPrice(weightedKNNPredictionCalculator(
					kNeighbour, instance));
		}
		return instance;
	}

	// predict price based on equation f(xq) = sumof f(xi) / k, i = 1 to k
	public Double calculatePrediction(List<Autos> kNeighbour) {
		double prediction = 0.0;
		for (Autos auto : kNeighbour) {
			prediction += auto.getActuralPrice();
		}
		prediction = prediction / kNeighbour.size();
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		prediction = Double.parseDouble(decimalFormat.format(prediction));
		return prediction;
	}

	public double accuracyCalculator(List<Autos> autosList) {
		int accurateInstances = 0;
		for (Autos auto : autosList) {
			if (auto.getActuralPrice() == auto.getPredictPrice()) {
				accurateInstances++;
			}
		}
		return (double) accurateInstances / autosList.size();
	}

	public double accuracyCalculator(List<Autos> autosList, double percentage) {
		int accurateInstances = 0;
		for (Autos auto : autosList) {
			if (auto.getActuralPrice() <= auto.getPredictPrice() * (1 + percentage)
					&& auto.getActuralPrice() >= auto.getPredictPrice()
							* (1 - percentage)) {
				accurateInstances++;
			}
		}
		return (double) accurateInstances / autosList.size();
	}	
	
	private void cleanUpPredictValue(List<Autos> dataSet) {
		for (Autos autos : dataSet)
			autos.setPredictPrice(0);
	}

	/**
	 * Distance functions
	 */
	//
	public double euclideanDistance(List<Double> data1, List<Double> data2) {
		double distance = 0;
		if (data1.size() == data2.size()) {
			for (int i = 0; i < data1.size() - 2; i++) {
				distance += Math.pow(data1.get(i) - data2.get(i), 2);
			}
		}
		distance = Math.sqrt(distance);
		if (!data1.get(data1.size() - 1).equals(data2.get(data1.size() - 1))) {
			distance++;
		}
		// System.out.println(distance);
		return distance;
	}

	// Manhattan Distance Measure
	public double ManhattanDistance(List<Double> data1, List<Double> data2) {
		double distance = 0;
		if (data1.size() == data2.size()) {
			for (int i = 0; i < data1.size() - 2; i++) {
				distance += Math.abs((data1.get(i) - data2.get(i)));
			}
		}
		if (!data1.get(data1.size() - 1).equals(data2.get(data1.size() - 1))) {
			distance++;
		}
		return distance;
	}

	// Canberra_distance
	public double CanDistance(List<Double> data1, List<Double> data2) {
		double distance = 0;
		if (data1.size() == data2.size()) {
			for (int i = 0; i < data1.size() - 2; i++) {
				distance += Math.abs((data1.get(i) - data2.get(i)))
						/ (Math.abs(data1.get(i)) + Math.abs(data2.get(i)));
			}
		}
		if (!data1.get(data1.size() - 1).equals(data2.get(data1.size() - 1))) {
			distance++;
		}
		return distance;
	}	
	
	public double weightedKNNPredictionCalculator(List<Autos> kNeighbour,
			Autos instance) {

		double prediction = 0.0;
		double w = 0.0;
		for (Autos auto : kNeighbour) {
			double weight = weightCalculator(euclideanDistance(
					auto.getNumericList(), instance.getNumericList()));
			prediction += auto.getActuralPrice() * weight;
			w += weight;
		}
		prediction = prediction / w;
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		prediction = Double.parseDouble(decimalFormat.format(prediction));
		return prediction;

	}

	public double weightCalculator(double distance) {
		if(distance == 0) {
			distance = 1;
		}
		return 1 / Math.pow(distance, 2);
	}
}
