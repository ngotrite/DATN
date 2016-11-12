package vn.edu.nuce.datn.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.dao.StudentDAO;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.util.ValidateUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "graPeriodBean")
@ViewScoped
public class GraduationPeriodBean extends BaseController implements Serializable {
	private GraduationPeriod graduationPeriod;
	private GraduationPeriodDAO graduationPeriodDAO;
	private List<GraduationPeriod> graduationPeriods;
	private UploadedFile file;

	private Student student;
	private List<Student> students;
	private StudentDAO studentDAO;
	
	private Boolean readOnly;
	
	@PostConstruct
	public void init() {
		this.graduationPeriod = new GraduationPeriod();
		this.graduationPeriodDAO = new GraduationPeriodDAO();
		this.graduationPeriods = new ArrayList<GraduationPeriod>();
		loadGraduatonPeriods();

		this.student = new Student();
		this.studentDAO = new StudentDAO();
		this.students = new ArrayList<Student>();
		
		this.readOnly = true;
	}
	
	// load list Student by Graduation Period
	public List<Student> loadStudentByGP(long graduationPeriodId) {
		students = new ArrayList<Student>();
		students = studentDAO.findSTByGP(graduationPeriodId);
		return students;
	}
	
	public void checkTimeToDisableStatus(GraduationPeriod item){
		if(item != null){
			Date nowDate = new Date();
			if(item.getStartDate().getTime() <= nowDate.getTime() && nowDate.getTime() <= item.getFinishDate().getTime()){
				this.readOnly = false;
			} else {
				this.readOnly = true;
			}
		}
	}
	
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("GraduationPeriod Selected", ((GraduationPeriod) event.getObject()).getGraduationPeriodName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        loadStudentByGP(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
        checkTimeToDisableStatus((GraduationPeriod) event.getObject());
    }
 
    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("GraduationPeriod Unselected", ((GraduationPeriod) event.getObject()).getGraduationPeriodName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        loadStudentByGP(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
    }

	/***** DIALOG STUDENT *****/

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
	
	public List<Student> lstStudens() {
		students = studentDAO.findAll();
		return students;
	}

	public void cmdDeleteStudent(Student student) {
		studentDAO.delete(student);
		students.remove(student);
		this.showMessageINFO("common.delete", "Student");
	}

	// Upload Student
	public void uploadExel(FileUploadEvent event) throws IOException {
		file = event.getFile();

		try {
			readStudentExcelFile(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private String getCellValue(Cell cell) {
		String cellValue;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellValue = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				cellValue = dateFormat.format(cell.getDateCellValue());
			} else {
				cellValue = Double.toString((long) cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BLANK:
			cellValue = "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellValue = Boolean.toString(cell.getBooleanCellValue());
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}

	// Import Subject Dictionary

	public void readStudentExcelFile(UploadedFile file) throws IOException {
		Workbook workbook = new HSSFWorkbook(file.getInputstream());

		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			if (nextRow.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			Student student = new Student();
			while (cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();

				switch (columnIndex) {
				case 0:
					student.setStudentId((String) getCellValue(nextCell));
					break;
				case 1:
					student.setStudentName((String) getCellValue(nextCell));
					break;
				case 2:
					student.set_class((String) getCellValue(nextCell));
					break;
				}

			}
			students.add(student);
		}
		workbook.close();
	}

	public void saveGraPeriod() {
		if (validateGraPeriod()) {
			graduationPeriodDAO.saveGraPeriod(graduationPeriod, students);
			this.showMessageINFO("common.save", "gp.graPeriod");
		}

	}

	// Validate
	private boolean validateGraPeriod() {
		boolean result = true;
		if (ValidateUtil.checkStringNullOrEmpty(graduationPeriod.getGraduationPeriodName())) {
			this.showMessageWARN("gp.graPeriod", super.readProperties("validate.checkValueNameNull"));
			result = false;
		}
		return result;
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

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
