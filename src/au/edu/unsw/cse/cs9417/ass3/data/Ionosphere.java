package au.edu.unsw.cse.cs9417.ass3.data;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import weka.core.Attribute;
import weka.core.Instance;

public class Ionosphere {

	final static int NUMOFATTRIBUTES = 34;
	private List<Double> list;
	private String actualClassification;
	private String predictClassification;

	public List<Double> getList() {
		return list;
	}

	public String getPredictClassification() {
		return predictClassification;
	}

	public void setPredictClassification(String predictClassification) {
		this.predictClassification = predictClassification;
	}

	public String getActualClassification() {
		return actualClassification;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Object item : this.list) {
			builder.append(item);
			builder.append(", ");
		}
		return "[" + builder.toString() + "]";
	}

	public Ionosphere() {

	}

	public Ionosphere(Instance instance) {
		this.list = new ArrayList<Double>(NUMOFATTRIBUTES);
		for (int i = 0; i < instance.numAttributes() - 1; i++) {
			this.list.add(instance.value(i));
		}
		this.actualClassification = instance.stringValue(34);
	}
}
