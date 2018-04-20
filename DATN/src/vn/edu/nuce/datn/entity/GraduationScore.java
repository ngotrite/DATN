package vn.edu.nuce.datn.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.enterprise.inject.Default;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenerationTime;

@SuppressWarnings("serial")
@Entity
@Table(name = "graduation_score")
public class GraduationScore implements Serializable {
	@Id
	@Column(name = "STUDENT_ID")
	private String studentId;

	@Column(name = "FILE_PATH")
	private String filePath;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "STATUS", nullable = false)
	@ColumnDefault(value = "false")
	private Boolean status;

	@Column(name = "CREATE_DATE",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false)
	@Temporal(TemporalType.TIMESTAMP)	
	private Date createDate;

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

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		if (Objects.isNull(createDate)) {
			this.createDate = new Date();
		} else {
			this.createDate = createDate;
		}
	}

}
