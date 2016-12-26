package vn.edu.nuce.datn.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "major")
public class Major implements Serializable {
	// @Id
	// @GeneratedValue(strategy = IDENTITY)
	// @Column(name = "ID")
	// private Long Id;
	@Id
//	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "MAJOR_ID")
	private String majorId;

	@Column(name = "MAJOR_NAME")
	private String majorName;

	public String getMajorId() {
		return majorId;
	}

	public void setMajorId(String majorId) {
		this.majorId = majorId;
	}

	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}

//	public Long getId() {
//		return Id;
//	}
//
//	public void setId(Long id) {
//		Id = id;
//	}

}
