package vn.edu.nuce.datn.entity;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DateToSearch implements Cloneable{
	private Calendar fromDate;
	private Calendar toDate;
	
	public DateToSearch() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DateToSearch(Calendar fromDate, Calendar toDate) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
	public Calendar getFromDate() {
		return fromDate;
	}
	public void setFromDate(Calendar fromDate) {
		this.fromDate = fromDate;
	}
	public Calendar getToDate() {
		return toDate;
	}
	public void setToDate(Calendar toDate) {
		this.toDate = toDate;
	}
}
