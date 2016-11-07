package vn.edu.nuce.datn.entity;

import java.io.Serializable;

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
	
	@Column(name = "CLASS")
	private String _class;
	
	@Column(name = "GRADUATION_PERIOD_ID")
	private Long graduationPeriodId;
	
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
}
