package au.edu.unsw.cse.cs9417.ass3;

import java.util.Map.Entry;
import java.util.Scanner;

import au.edu.unsw.cse.cs9417.ass3.util.FileReader;

public class Entrance {

	public static void main(String[] args) {

		FileReader reader = new FileReader();
		Entry<Integer, Double> entry = null;
		while (true) {
			int levelOneChooice, distanceFunction;
			double accuracyRange;
			boolean weitghtFunction;
			printLevelOneUI();
			Scanner scanner = new Scanner(System.in);
			levelOneChooice = scanner.nextInt();
			printLevelTwoUI();
			distanceFunction = scanner.nextInt();
			switch (levelOneChooice) {
			case 1:
				entry = reader.readForIonosphere(distanceFunction);
				// System.out.println("k=" + entry.getKey()
				// + ", maxmium accuracy rate= " + entry.getValue());
				break;
			case 2:
				printLevelThreeUI();
				accuracyRange = scanner.nextDouble();
				printLevelFourUI();
				weitghtFunction = scanner.nextBoolean();
				entry = reader.readForAutos(distanceFunction, accuracyRange,
						weitghtFunction);
				// System.out.println("k=" + entry.getKey()
				// + ", maxmium accuracy rate= " + entry.getValue());
				break;
			default:
				printLevelOneUI();
				break;
			}
		}
	}

	private static void printLevelOneUI() {
		System.out.println("=========================================");
		System.out.println("*     K-Nearest Neighbour Algorithm     *");
		System.out.println("*                                       *");
		System.out.println("* 1. Classification on ionosphere.arff  *");
		System.out.println("* 2. Prediction on autos.arff           *");
		System.out.println("*                                       *");
		System.out.println("* Please input your choice:             *");
		System.out.println("*                                       *");
		System.out.println("=========================================");
	}

	private static void printLevelTwoUI() {
		System.out.println("=========================================");
		System.out.println("*     K-Nearest Neighbour Algorithm     *");
		System.out.println("* Please select distance function:      *");
		System.out.println("* 1. Euclidean Distance                 *");
		System.out.println("* 2. Manhattan Distance                 *");
		System.out.println("* 3. Canberra Distance                  *");
		System.out.println("*                                       *");
		System.out.println("* Please input your choice:             *");
		System.out.println("*                                       *");
		System.out.println("=========================================");
	}

	private static void printLevelThreeUI() {
		System.out.println("=========================================");
		System.out.println("*     K-Nearest Neighbour Algorithm     *");
		System.out.println("* Please input accuracy range(eg: 0.1): *");
		System.out.println("*                                       *");
		System.out.println("=========================================");
	}

	private static void printLevelFourUI() {
		System.out.println("===========================================");
		System.out.println("*      K-Nearest Neighbour Algorithm      *");
		System.out.println("* Do you want use weighted function?      *");
		System.out.println("* Please input your chooice: (True/False) *");
		System.out.println("*                                         *");
		System.out.println("===========================================");
	}

}
