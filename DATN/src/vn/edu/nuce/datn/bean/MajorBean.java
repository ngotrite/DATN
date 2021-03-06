package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;

import vn.edu.nuce.datn.dao.CertificateDAO;
import vn.edu.nuce.datn.dao.MajorDAO;
import vn.edu.nuce.datn.entity.Major;
import vn.edu.nuce.datn.entity.News;

@SuppressWarnings("serial")
@ManagedBean(name = "majorBean")
@ViewScoped
public class MajorBean extends BaseController implements Serializable {

	private Major major;
	private MajorDAO majorDAO;
	private List<Major> lstMajor;
	private Boolean isEdit;
	

	@PostConstruct
	public void init() {
		this.lstMajor = new ArrayList<Major>();
		this.major = new Major();
		this.majorDAO = new MajorDAO();
		loadLstMajor();
	}

	public void loadLstMajor() {
		lstMajor = majorDAO.findAll();
	}

	public void addNewMajor() {
		major = new Major();
	}

	public void applyMajor() {
		try {
			majorDAO.saveOrUpdate(major);
			loadLstMajor();
			super.showNotificationSuccsess();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void editMajor(Major major) {
		this.major = major;
	}

	public void deleleMajor(Major major) {
		CertificateDAO cDAO = new CertificateDAO();
		if (!cDAO.checkMajorInCertificate(major.getMajorId())) {
			majorDAO.delete(major);
			lstMajor.remove(major);
			major = new Major();
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form-major:dtMajor");
			if (!dataTable.getFilters().isEmpty()) {
				dataTable.reset();// working
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.update("form-major:dtMajor");
			}
			this.showMessageINFO("common.delete", "Ng�nh");
		} else {
			this.showMessageWARN("common.summary.warning", super.readProperties("validate.fieldUseIn"));
		}
	}

	public void cancelMajor() {
		major = new Major();
	}

	public Major getMajor() {
		return major;
	}

	public void setMajor(Major major) {
		this.major = major;
	}

	public List<Major> getLstMajor() {
		return lstMajor;
	}

	public void setLstMajor(List<Major> lstMajor) {
		this.lstMajor = lstMajor;
	}
}
