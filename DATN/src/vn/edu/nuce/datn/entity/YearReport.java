package vn.edu.nuce.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YearReport implements Cloneable{
	private String year;
	private String value;
	
	public YearReport(String year, String value) {
		super();
		this.year = year;
		this.value = value;
	}
	public YearReport() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
