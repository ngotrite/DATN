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
@Table(name = "document")
public class Document implements Serializable {
	@Id
	@GeneratedValue(strategy=IDENTITY)
	@Column(name = "DOCUMENT_ID")
	private Long documentId;
	
	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "ISSUANCE_DATE")
	private Date issuanceDate;
	
	@Column(name = "FILE_NAME")
	private String fileName;
	
	@Column(name = "FILE_PATH")
	private String filePath;

	@Column(name = "UPLOAD_DATE")
	private Date uploadDate;
	
	@Column(name = "UPLOAD_USER_ID")
	private Long uploadUserId;
	
	@Column(name = "NUMBER_DOWNLOAD")
	private Long numberDL;

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getIssuanceDate() {
		return issuanceDate;
	}

	public void setIssuanceDate(Date issuanceDate) {
		this.issuanceDate = issuanceDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Long getUploadUserId() {
		return uploadUserId;
	}

	public void setUploadUserId(Long uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	public Long getNumberDL() {
		return numberDL;
	}

	public void setNumberDL(Long numberDL) {
		this.numberDL = numberDL;
	}
}
