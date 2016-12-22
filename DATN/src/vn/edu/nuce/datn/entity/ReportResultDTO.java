
package vn.edu.nuce.datn.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportResultDTO implements Cloneable {

	private String majorName;
	private String issDate1;
	private String issDate2;
	private String issDate3;
	private String issDate4;
	private String issDate5;
	
	

	public ReportResultDTO() {
		super();
	}

	public ReportResultDTO(String majorName, String issDate1, String issDate2, String issDate3, String issDate4,
			String issDate5) {
		super();
		this.majorName = majorName;
		this.issDate1 = issDate1;
		this.issDate2 = issDate2;
		this.issDate3 = issDate3;
		this.issDate4 = issDate4;
		this.issDate5 = issDate5;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

	public String getIssDate1() {
		return issDate1;
	}

	public void setIssDate1(String issDate1) {
		this.issDate1 = issDate1;
	}

	public String getIssDate2() {
		return issDate2;
	}

	public void setIssDate2(String issDate2) {
		this.issDate2 = issDate2;
	}

	public String getIssDate3() {
		return issDate3;
	}

	public void setIssDate3(String issDate3) {
		this.issDate3 = issDate3;
	}

	public String getIssDate4() {
		return issDate4;
	}

	public void setIssDate4(String issDate4) {
		this.issDate4 = issDate4;
	}

	public String getIssDate5() {
		return issDate5;
	}

	public void setIssDate5(String issDate5) {
		this.issDate5 = issDate5;
	}

}
