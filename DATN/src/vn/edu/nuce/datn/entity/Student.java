package vn.edu.nuce.datn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@SuppressWarnings("serial")
@Entity
@Table(name = "student")
public class Student implements Serializable{
	
	@Id 
	@Column(name = "STUDENT_ID")
	private String studentId;
	
	@Column(name = "STUDENT_NAME")
	private String studentName;
	
	@Column(name = "_CLASS")
	private String _class;
	
	@Column(name = "GRADUATION_PERIOD_ID")
	private Long graduationPeriodId;
	
	@Column(name = "SCHOOL_FEE_STATUS")
	private Boolean schoolFeeStatus;
	
	@Column(name = "SCHOOL_FEE_USER_ID")
	private Long schoolFeeUserId;
	
	@Column(name = "SCHOOL_FEE_UPDATE_TIME")
	private Date schoolFeeUpdateTime;
	
	@Column(name = "LIBRARY_STATUS")
	private Boolean libraryStatus;
	
	@Column(name = "LIBRARY_USER_ID")
	private Long libraryUserId;
	
	@Column(name = "LIBRARY_UPDATE_TIME")
	private Date libraryUpdateTime;
	
	@Column(name = "DEPARTMENT_STATUS")
	private Boolean departmentStatus;
	
	@Column(name = "DEPARTMENT_USER_ID")
	private Long departmentUserId;
	
	@Column(name = "DEPARTMENT_UPDATE_TIME")
	private Date departmentUpdateTime;
	
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
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	
	public Long getGraduationPeriodId() {
		return graduationPeriodId;
	}
	public void setGraduationPeriodId(Long graduationPeriodId) {
		this.graduationPeriodId = graduationPeriodId;
	}
	public Boolean getSchoolFeeStatus() {
		return schoolFeeStatus;
	}
	public void setSchoolFeeStatus(Boolean schoolFeeStatus) {
		this.schoolFeeStatus = schoolFeeStatus;
	}
	public Long getSchoolFeeUserId() {
		return schoolFeeUserId;
	}
	public void setSchoolFeeUserId(Long schoolFeeUserId) {
		this.schoolFeeUserId = schoolFeeUserId;
	}
	public Date getSchoolFeeUpdateTime() {
		return schoolFeeUpdateTime;
	}
	public void setSchoolFeeUpdateTime(Date schoolFeeUpdateTime) {
		this.schoolFeeUpdateTime = schoolFeeUpdateTime;
	}
	public Boolean getLibraryStatus() {
		return libraryStatus;
	}
	public void setLibraryStatus(Boolean libraryStatus) {
		this.libraryStatus = libraryStatus;
	}
	public Long getLibraryUserId() {
		return libraryUserId;
	}
	public void setLibraryUserId(Long libraryUserId) {
		this.libraryUserId = libraryUserId;
	}
	public Date getLibraryUpdateTime() {
		return libraryUpdateTime;
	}
	public void setLibraryUpdateTime(Date libraryUpdateTime) {
		this.libraryUpdateTime = libraryUpdateTime;
	}
	public Boolean getDepartmentStatus() {
		return departmentStatus;
	}
	public void setDepartmentStatus(Boolean departmentStatus) {
		this.departmentStatus = departmentStatus;
	}
	public Long getDepartmentUserId() {
		return departmentUserId;
	}
	public void setDepartmentUserId(Long departmentUserId) {
		this.departmentUserId = departmentUserId;
	}
	public Date getDepartmentUpdateTime() {
		return departmentUpdateTime;
	}
	public void setDepartmentUpdateTime(Date departmentUpdateTime) {
		this.departmentUpdateTime = departmentUpdateTime;
	}
}
