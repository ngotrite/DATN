package vn.edu.nuce.datn.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "graduation_score")
public class GraduationScore implements Serializable{
	@Id
	@Column(name = "STUDENT_ID")
	private String studentId;
	
	@Column(name = "FILE_PATH")
	private String filePath;
	
	@Column(name = "FILE_NAME")
	private String fileName;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
