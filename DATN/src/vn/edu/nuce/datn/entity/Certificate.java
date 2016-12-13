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
	private Date birthday;
	
	@Column(name = "BIRTH_PLACE")
	private String birthPlace;

}
