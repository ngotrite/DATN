
package vn.edu.nuce.datn.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportResultDTO implements Cloneable {

	private String majorName;
	private List<YearReport> yearReport;

	
	

	public ReportResultDTO() {
		super();
	}



	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public List<YearReport> getYearReport() {
		return yearReport;
	}

	public void setYearReport(List<YearReport> yearReport) {
		this.yearReport = yearReport;
	}
	
	

	
}
