package vn.edu.nuce.datn.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.dao.StudentDAO;
import vn.edu.nuce.datn.dao.SysUserDAO;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.entity.SysUser;
import vn.edu.nuce.datn.util.SessionUtils;
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
	private List<Student> filteredStudents;
	private List<Student> finishedStudents;
	private StudentDAO studentDAO;

	private Boolean readOnly;

	private List<SelectItem> lstFillter;

	@PostConstruct
	public void init() {
		this.graduationPeriod = new GraduationPeriod();
		this.graduationPeriodDAO = new GraduationPeriodDAO();
		this.graduationPeriods = new ArrayList<GraduationPeriod>();
		loadGraduatonPeriods();

		this.student = new Student();
		this.studentDAO = new StudentDAO();
		this.students = new ArrayList<Student>();
		this.finishedStudents = new ArrayList<Student>();
		this.readOnly = true;
	}
	
	public String getUserName(Long id) {
		SysUserDAO dao = new SysUserDAO();
		id = SessionUtils.getUser().getId();
		if (id != null) {
			return dao.get(id).getUserName();
		}
		return "";
	}
	
	
	public int countStudentsSF() {
		if (graduationPeriod.getGraduationPeriodId() != null) {
			for (Student student : students) {
				if (student.getSchoolFeeStatus() != null && student.getSchoolFeeStatus() == true) {
					int a = studentDAO.countStudentsSF(graduationPeriod.getGraduationPeriodId(), true);
					return a;
				}
			}

		}
		return 0;
	}

	public int countStudentsD() {
		if (graduationPeriod.getGraduationPeriodId() != null) {
			for (Student student : students) {
				if (student.getDepartmentStatus() != null && student.getDepartmentStatus() == true) {
					int b = studentDAO.countStudentsD(graduationPeriod.getGraduationPeriodId(), true);
					return b;
				}
			}
		}
		return 0;
	}

	public int countStudentsL() {
		if (graduationPeriod.getGraduationPeriodId() != null) {
			for (Student student : students) {
				if (student.getLibraryStatus() != null && student.getLibraryStatus() == true) {
					int c = studentDAO.countStudentsL(graduationPeriod.getGraduationPeriodId(), true);
					return c;
				}
			}
		}
		return 0;
	}

	public void postProcessXLS(Object document) {
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header = sheet.getRow(0);

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			HSSFCell cell = header.getCell(i);

			cell.setCellStyle(cellStyle);
		}
	}

	public void resetFilters() {
		DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
				.findComponent(":form-sd-list:dtStudent");
		if (dataTable != null) {
			dataTable.resetValue();
			dataTable.reset();
			dataTable.setFilters(null);
			student.setSchoolFeeStatus(null);
			student.setDepartmentStatus(null);
			student.setLibraryStatus(null);
		}
	}

	public List<Student> loadStudentUnFinishedByGP(long graduationPeriodId) {
		students = new ArrayList<Student>();
		students = studentDAO.findSTUFinishedByGP(graduationPeriodId);
		return students;
	}

	public void saveStudent() {
		studentDAO.saveStudents(students);
		super.showNotificationSuccsess();
		countStudentsSF();
		countStudentsD();
		countStudentsL();
	}

	public void showSumStudent() {
		this.showMessageINFO("Students : ", filteredStudents.size() + "");
	}

	public void autoUpdateDate(Student student) {
		if (student.getDepartmentStatus() != null && student.getDepartmentStatus() == true) {
			student.setDepartmentUpdateTime(new Date());
			student.setDepartmentUserId(SessionUtils.getUser().getId());
		} else {
			student.setDepartmentUpdateTime(null);
		}
		if (student.getLibraryStatus() != null && student.getLibraryStatus() == true) {
			student.setLibraryUpdateTime(new Date());
			student.setLibraryUserId(SessionUtils.getUser().getId());
		} else {
			student.setLibraryUpdateTime(null);
		}
		if (student.getSchoolFeeStatus() != null && student.getSchoolFeeStatus() == true) {
			student.setSchoolFeeUpdateTime(new Date());
			student.setSchoolFeeUserId(SessionUtils.getUser().getId());
		} else {
			student.setSchoolFeeUpdateTime(null);
		}
	}

	public void cmdDeleteGP(GraduationPeriod graduationPeriod) {
		graduationPeriodDAO.delete(graduationPeriod);
		graduationPeriods.remove(graduationPeriod);
		super.showNotificationSuccsess();
	}

	public void showDialogGP(GraduationPeriod graduationPeriod) {
		if (graduationPeriod == null) {
			this.graduationPeriod = new GraduationPeriod();
			this.graduationPeriod.setStartDate(new Date());
			this.graduationPeriod.setFinishDate(new Date());
			this.students.clear();
		} else {
			this.graduationPeriod = graduationPeriod;
			loadStudentByGP(this.graduationPeriod.getGraduationPeriodId());
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgGPWV').show();");
	}

	// load list Student by Graduation Period
	public List<Student> loadStudentByGP(long graduationPeriodId) {
		students = new ArrayList<Student>();
		students = studentDAO.findSTByGP(graduationPeriodId);
		return students;
	}

	// load list Student Finished by Graduation Period
	public List<Student> loadStudentFinishedByGP(long graduationPeriodId) {
		finishedStudents = new ArrayList<Student>();
		finishedStudents = studentDAO.findSTFinishedByGP(graduationPeriodId);
		return finishedStudents;
	}

	public void checkTimeToDisableStatus(GraduationPeriod item) {
		if (item != null) {
			Date nowDate = new Date();
			if (item.getStartDate().getTime() < nowDate.getTime()
					&& nowDate.getTime() < item.getFinishDate().getTime()) {
				item.setStatus(true);
				this.readOnly = false;
			} else {
				this.readOnly = true;
				item.setStatus(false);
			}
		}
	}

	public void onRowSelect(SelectEvent event) {
		loadStudentByGP(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
		checkTimeToDisableStatus((GraduationPeriod) event.getObject());
		countStudentsSF();
		countStudentsD();
		countStudentsL();
	}

	public void onRowUnselect(UnselectEvent event) {
		loadStudentByGP(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
	}

	/***** DIALOG STUDENT *****/

	public void showDialogStudent(Student student) {
		if (student == null) {
			student = new Student();
			this.student = new Student();
		} else {
			this.student = student;
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgStudentWV').show();");

	}

	public void cmdApplyDLGStudent() {
		graduationPeriodDAO.saveGraPeriod(graduationPeriod, students);
		// studentDAO.saveOrUpdate(student);
		students.add(student);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgStudentWV').hide();");

	}

	public void showDialogStudentFinished(GraduationPeriod graduationPeriod) {
		if (graduationPeriod == null) {
			graduationPeriod = new GraduationPeriod();
		} else {
			this.graduationPeriod = graduationPeriod;
			loadStudentFinishedByGP(graduationPeriod.getGraduationPeriodId());
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgStudentFinnishedWV').show();");

	}

	/***** STUDENT *****/

	private void loadStudents() {
		students = studentDAO.findAll();
	}

	public List<Student> lstStudens() {
		students = studentDAO.findAll();
		readOnly = true;
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
				student.setDepartmentStatus(false);
				student.setLibraryStatus(false);
				student.setSchoolFeeStatus(false);

			}
			students.add(student);
		}
		workbook.close();
	}

	public void saveGraPeriod() {
		if (validateGraPeriod()) {
			checkTimeToDisableStatus(graduationPeriod);
			graduationPeriodDAO.saveGraPeriod(graduationPeriod, students);
			super.showNotificationSuccsess();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgGPWV').hide();");
			checkTimeToDisableStatus(graduationPeriod);
			loadGraduatonPeriods();
			countStudentsSF();
			countStudentsD();
			countStudentsL();
		}

	}

	// Validate
	private boolean validateGraPeriod() {
		boolean result = true;
		if (ValidateUtil.checkStringNullOrEmpty(graduationPeriod.getGraduationPeriodName())) {
			this.showMessageWARN("", super.readProperties("validate.checkValueNameNull"));
			result = false;
		} else if (graduationPeriod.getStartDate() != null && graduationPeriod.getFinishDate() != null
				&& graduationPeriod.getStartDate().getTime() > graduationPeriod.getFinishDate().getTime()) {
			this.showMessageWARN("", super.readProperties("validate.startDateBeforeFinishDate"));
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

	public List<Student> getFilteredStudents() {
		return filteredStudents;
	}

	public void setFilteredStudents(List<Student> filteredStudents) {
		this.filteredStudents = filteredStudents;
	}

	public List<SelectItem> getLstFillter() {
		return lstFillter;
	}

	public void setLstFillter(List<SelectItem> lstFillter) {
		this.lstFillter = lstFillter;
	}

	public List<Student> getFinishedStudents() {
		return finishedStudents;
	}

	public void setFinishedStudents(List<Student> finishedStudents) {
		this.finishedStudents = finishedStudents;
	}

}
