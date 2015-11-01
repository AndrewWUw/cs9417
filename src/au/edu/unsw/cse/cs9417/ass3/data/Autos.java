package au.edu.unsw.cse.cs9417.ass3.data;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.core.Attribute;
import weka.core.Instance;

public class Autos {

	private List<Double> numericList; // list contains only numeric attributes
	private List<Object> attributeList; // list contains all attributes
	private Map<String, Object> attributes; // map that contains all attribute
											// name-attribute value pairs
	private double acturalPrice; // actual price of the car
	private double predictPrice; // predict price
	private int id;

	public double getPredictPrice() {
		return predictPrice;
	}

	public void setPredictPrice(double predictPrice) {
		this.predictPrice = predictPrice;
	}

	public double getActuralPrice() {
		return acturalPrice;
	}

	public List<Double> getNumericList() {
		return numericList;
	}

	public List<Object> getAttributeList() {
		return attributeList;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public Autos() {

	}

	public Autos(List<Double> numericList, List<Object> attributeList) {
		super();
		this.numericList = numericList;
		this.attributeList = attributeList;
	}

	public Autos(Instance instance, int id) {

		this.numericList = new ArrayList<Double>();
		this.attributeList = new ArrayList<Object>();
		this.attributes = new HashMap<String, Object>();
		this.id = id;
		Enumeration<Attribute> att = instance.enumerateAttributes();
		// System.out.println(instance.numAttributes());

		while (att.hasMoreElements()) {
			Attribute a = att.nextElement();
			// System.out.println(a);
			if (a.isNumeric() && !a.name().equals("price")) {
				this.numericList.add(instance.value(a));
				this.attributeList.add(instance.value(a));
				this.attributes.put(a.name(), instance.value(a));
			} else if (a.isNumeric() && a.name().equals("price")) {
				this.acturalPrice = instance.value(a);
				this.attributeList.add(instance.value(a));
				this.attributes.put(a.name(), instance.value(a));
			} else if (a.isNominal()) {
				this.attributeList.add(instance.stringValue(a));
				this.attributes.put(a.name(), instance.stringValue(a));
			}
		}

		Attribute a = instance.attribute(25);
		this.numericList.add(Double.parseDouble(instance.stringValue(25)));
		this.attributeList.add(Double.parseDouble(instance.stringValue(25)));
		this.attributes.put(a.name(),
				Double.parseDouble(instance.stringValue(25)));
	}

	@Override
	public String toString() {
		return "id=" + id;
	}
	
	
}
