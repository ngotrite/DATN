
package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import vn.edu.nuce.datn.dao.StudentDAO;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.entity.SubjectDictionary;

@SuppressWarnings("serial")
@ManagedBean(name = "studentBean")
@ViewScoped
public class StudentBean extends BaseController implements Serializable{
	
	private Student student;
	private List<Student> students;
	private StudentDAO studentDAO;
	
	@PostConstruct
	public void init(){
		this.student = new Student();
		this.studentDAO = new StudentDAO();
		this.students = new ArrayList<Student>();
		loadStudents();
	}
	
	/***** DIALOG STUDENT*****/

	public void showDialogStudent(Student student) {
		if (student == null) {
			student = new Student();
		}
		this.student = student;

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgStudentWV').show();");

	}
	
	/***** STUDENT *****/
	
	private void loadStudents() {
		students = studentDAO.findAll();
	}
	
	public void cmdDeleteStudent(Student student) {
		studentDAO.delete(student);
		students.remove(student);
		this.showMessageINFO("common.delete", "Student");
	}
	
	
	//  GET SET 
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
	
	

}
