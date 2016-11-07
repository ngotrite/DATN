	package vn.edu.nuce.datn.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "test_score")
public class TestScore implements Serializable {
	
	
	@Id 
	@GeneratedValue(strategy=IDENTITY)
	@Column(name = "TEST_SCORE_ID")
	private Long testScoreId;
	
	
	@Column(name = "SUBJECT_ID")
	private String subjectId;
	
	@Column(name = "GROUP_ID")
	private String groupId;

	@Column(name = "SCHOOL_YEAR")
	private String schoolYear;
	
	@Column(name = "TEST")
	private String test;
	
	@Column(name = "FILE_PATH")
	private String filePath;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;
	
	@Column(name = "REMARK")
	private String remark;
	
	
	
	public Long getTestScoreId() {
		return testScoreId;
	}

	public void setTestScoreId(Long testScoreId) {
		this.testScoreId = testScoreId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}	

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
