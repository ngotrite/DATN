package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.entity.GraduationPeriod;

@SuppressWarnings("serial")
public class GraduationPeriodBean extends BaseController implements Serializable{
	private GraduationPeriod graduationPeriod;
	private GraduationPeriodDAO graduationPeriodDAO;
	private List<GraduationPeriod> graduationPeriods;
	
	@PostConstruct
	public void init(){
		this.graduationPeriod = new GraduationPeriod();
		this.graduationPeriodDAO = new GraduationPeriodDAO();
		this.graduationPeriods = new ArrayList<GraduationPeriod>();
		loadGraduatonPeriods();
	}
	
	private void loadGraduatonPeriods() {
		graduationPeriods = graduationPeriodDAO.findAll();
	}
	
	// GET SET

	public GraduationPeriod getGraduationPeriod() {
		return graduationPeriod;
	}

	public void setGraduationPeriod(GraduationPeriod graduationPeriod) {
		this.graduationPeriod = graduationPeriod;
	}

	public GraduationPeriodDAO getGraduationPeriodDAO() {
		return graduationPeriodDAO;
	}

	public void setGraduationPeriodDAO(GraduationPeriodDAO graduationPeriodDAO) {
		this.graduationPeriodDAO = graduationPeriodDAO;
	}

	public List<GraduationPeriod> getGraduationPeriods() {
		return graduationPeriods;
	}

	public void setGraduationPeriods(List<GraduationPeriod> graduationPeriods) {
		this.graduationPeriods = graduationPeriods;
	}
}
