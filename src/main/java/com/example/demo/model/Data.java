//Represents data fields from both CSV(id,value) and API(id,name)
package com.example.demo.model;

public class Data {
	private int id;
	private String name;
	private double value;

	public Data(int id, String name, double value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}

	public Data() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", name=" + name + ", value=" + value + "]";
	}

}
