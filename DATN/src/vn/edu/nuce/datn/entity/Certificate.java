package vn.edu.nuce.datn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "certificate")
public class Certificate implements Serializable{
	
	@Id 
	@Column(name = "STUDENT_ID")
	private String studentId;
	
	@Column(name = "STUDENT_NAME")
	private String studentName;
	
	@Column(name = "BIRTHDAY")
	private String birthday;
	
	@Column(name = "BIRTH_PLACE")
	private String birthPlace;
	
	@Column(name = "EDUCATION_SYSTEM")
	private String educationSystem;
	
	@Column(name = "PROGRAM")
	private String program;
	
	@Column(name = "MAJOR")
	private String major;
	
	@Column(name = "CERTIFICATE_NO")
	private String certificateNo;
	
	@Column(name = "GRADUATION_YEAR")
	private String graduationYear;
	
	@Column(name = "ISSUANCE_DATE")
	private String issuanceDate;
	
	@Column(name = "GRADE")
	private String grade;
	
	@Column(name = "UPDATE_DATE")
	private Date updateDate;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getEducationSystem() {
		return educationSystem;
	}

	public void setEducationSystem(String educationSystem) {
		this.educationSystem = educationSystem;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getCertificateNo() {
		return certificateNo;
	}

	public void setCertificateNo(String certificateNo) {
		this.certificateNo = certificateNo;
	}

	public String getGraduationYear() {
		return graduationYear;
	}

	public void setGraduationYear(String graduationYear) {
		this.graduationYear = graduationYear;
	}

	public String getIssuanceDate() {
		return issuanceDate;
	}

	public void setIssuanceDate(String issuanceDate) {
		this.issuanceDate = issuanceDate;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
}
