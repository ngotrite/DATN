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
@Table(name = "graduation_period")
public class GraduationPeriod implements Serializable{
	@Id
	@GeneratedValue(strategy=IDENTITY)
	@Column(name = "GRADUATION_PERIOD_ID")
	private Long graduationPeriodId;
	
	@Column(name = "GRADUATION_PERIOD_NAME")
	private String graduationPeriodName;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "FINISH_DATE")
	private Date finishDate;
	
	@Column(name = "STATUS")
	private Boolean status;
	
	@Column(name = "REMARK")
	private String remark;

	public Long getGraduationPeriodId() {
		return graduationPeriodId;
	}

	public void setGraduationPeriodId(Long graduationPeriodId) {
		this.graduationPeriodId = graduationPeriodId;
	}

	public String getGraduationPeriodName() {
		return graduationPeriodName;
	}

	public void setGraduationPeriodName(String graduationPeriodName) {
		this.graduationPeriodName = graduationPeriodName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
