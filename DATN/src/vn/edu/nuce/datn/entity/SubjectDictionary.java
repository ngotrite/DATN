package vn.edu.nuce.datn.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "sub_dictionary")
public class SubjectDictionary implements Serializable {

	@Id
	@Column(name = "SUBJECT_ID")
	private String subjectId;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "CREDIT")
	private Long credit;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getCredit() {
		return credit;
	}

	public void setCredit(Long credit) {
		this.credit = credit;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
}
