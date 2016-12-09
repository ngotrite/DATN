
package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.dao.StudentDAO;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;

@SuppressWarnings("serial")
@ManagedBean(name = "studentBean")
@ViewScoped
public class StudentBean extends BaseController implements Serializable {

	private Student student;
	private List<Student> students;
	private StudentDAO studentDAO;
	private GraduationPeriod gp;
	private List<GraduationPeriod> lstGP;

	@PostConstruct
	public void init() {
		this.student = new Student();
		this.studentDAO = new StudentDAO();
		this.students = new ArrayList<Student>();
		this.gp = new GraduationPeriod();
		this.lstGP = new ArrayList<GraduationPeriod>();
		loadStudents();
	}

	public String getGPName(Long graduationPeriodId) {
		GraduationPeriodDAO gPDAO = new GraduationPeriodDAO();
		if (graduationPeriodId != null) {
			return gPDAO.get(graduationPeriodId).getGraduationPeriodName();
		}
		return null;
	}

	/***** STUDENT *****/

	private void loadStudents() {
		students = studentDAO.findAll();
	}

	// GET SET
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public StudentDAO getStudentDAO() {
		return studentDAO;
	}

	public void setStudentDAO(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}

	public GraduationPeriod getGp() {
		return gp;
	}

	public void setGp(GraduationPeriod gp) {
		this.gp = gp;
	}

	public List<GraduationPeriod> getLstGP() {
		return lstGP;
	}

	public void setLstGP(List<GraduationPeriod> lstGP) {
		this.lstGP = lstGP;
	}
}
