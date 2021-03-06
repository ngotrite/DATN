package vn.edu.nuce.datn.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.dao.GraduationScoreDAO;
import vn.edu.nuce.datn.dao.StudentDAO;
import vn.edu.nuce.datn.dao.SysUserDAO;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.util.ContantsUtil;
import vn.edu.nuce.datn.util.SessionUtils;

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
	private List<Student> selectionStudents;
	private StudentDAO studentDAO;

	private Boolean readOnly;

	private List<SelectItem> lstFillter;
	public Boolean isEdit;
	private Date toDate;

	private List<Student> lstImport;

	private List<Student> lstStudentByGraNotComplete;
	
	@PostConstruct
	public void init() {
		this.graduationPeriod = new GraduationPeriod();
		this.graduationPeriodDAO = new GraduationPeriodDAO();
		this.graduationPeriods = new ArrayList<GraduationPeriod>();

		this.student = new Student();
		this.studentDAO = new StudentDAO();
		this.students = new ArrayList<Student>();
		this.finishedStudents = new ArrayList<Student>();
		this.selectionStudents = new ArrayList<Student>();
		this.readOnly = true;
		this.isEdit = false;
		this.toDate = new Date();
		this.lstImport = new ArrayList<Student>();
		loadGraduatonPeriods();
		this.students.size();
		this.lstStudentByGraNotComplete = new ArrayList<Student>();
	}
	
	public String createReportStudent() {
		
		
		return "";
	}
	

	public String viewInfoStu(Student item, String type) {
		StudentDAO studentDAO = new StudentDAO();
		if (item.getStudentId() != null && studentDAO.checkStudentId(item.getStudentId())) {
			switch (type) {
			case ContantsUtil.InfoStu.STUDENT_NAME:
				return studentDAO.get(item.getStudentId()).getStudentName();
			case ContantsUtil.InfoStu._CLASS:
				return studentDAO.get(item.getStudentId()).get_class();
			default:
				break;
			}
			return "";
		}
		return "";

	}

	public void updateStudentsB() throws IOException {
		List<String> studentIds = new ArrayList<>();

		for (Student student : lstImport) {
			studentIds.add(student.getStudentId());
		}

		if (studentIds.size() > 0) {
			studentDAO.updateStudentStatusB(studentIds, new Date(), SessionUtils.getUser().getId());
		}
		loadGraduatonPeriods();
		countStudentsB();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wvIDepartment').hide();");
		this.showNotificationSuccsess();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
		ec.redirect(req.getContextPath() + "/admin/lst_graduation_period.xhtml");
	}

	public void updateStudentsO() throws IOException {
		List<String> studentIds = new ArrayList<>();

		for (Student student : lstImport) {
			studentIds.add(student.getStudentId());
		}

		if (studentIds.size() > 0) {
			studentDAO.updateStudentStatusO(studentIds, new Date(), SessionUtils.getUser().getId());
		}
		loadGraduatonPeriods();
		countStudentsO();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wvIDepartment').hide();");
		this.showNotificationSuccsess();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
		ec.redirect(req.getContextPath() + "/admin/lst_graduation_period_ocom.xhtml");
	}

	public void updateStudentsL() throws IOException {
		List<String> studentIds = new ArrayList<>();

		for (Student student : lstImport) {
			studentIds.add(student.getStudentId());
		}

		if (studentIds.size() > 0) {
			studentDAO.updateStudentStatusL(studentIds, new Date(), SessionUtils.getUser().getId());
		}
		loadGraduatonPeriods();
		countStudentsL();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wvIDepartment').hide();");
		this.showNotificationSuccsess();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
		ec.redirect(req.getContextPath() + "/admin/lst_graduation_period_library.xhtml");
	}

	public void updateStudentsF() throws IOException {
		List<String> studentIds = new ArrayList<>();

		for (Student student : lstImport) {
			studentIds.add(student.getStudentId());
		}

		if (studentIds.size() > 0) {
			studentDAO.updateStudentStatusF(studentIds, new Date(), SessionUtils.getUser().getId());
		}
		loadGraduatonPeriods();
		countStudentsSF();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wvIDepartment').hide();");
		this.showNotificationSuccsess();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
		ec.redirect(req.getContextPath() + "/admin/lst_graduation_period_finance.xhtml");
	}

	public void updateStudents() throws IOException {
		List<String> studentIds = new ArrayList<>();

		for (Student student : lstImport) {
			studentIds.add(student.getStudentId());
		}

		if (studentIds.size() > 0) {
			studentDAO.updateStudentStatus(studentIds, new Date(), SessionUtils.getUser().getId());
		}
		loadGraduatonPeriods();
		loadStudentByGP(graduationPeriod.getGraduationPeriodId());
		countStudentsD();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wvIDepartment').hide();");
		this.showNotificationSuccsess();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
		ec.redirect(req.getContextPath() + "/admin/lst_graduation_period_department.xhtml");
	}

	public void uploadDep(FileUploadEvent event) throws IOException {
		file = event.getFile();

		try {
			readIDep(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readIDep(UploadedFile file) throws IOException {
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
					String studentId = getCellValue(nextCell).toString();
					if (studentId.indexOf(".") != -1) {
						studentId = studentId.substring(0, studentId.indexOf("."));
					}
					if (Objects.nonNull(studentId) && !studentId.trim().isEmpty()) {
						student.setStudentId(studentId);
					}
					break;
				}
			}
			lstImport.add(student);
		}
		workbook.close();
	}

	public void showDLGIDep() {
		lstImport.clear();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('wvIDepartment').show();");
	}

	public void removeAll() {
		studentDAO.delListStudent(students);
		students.clear();
		super.showNotificationSuccsess();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgGPWV').hide();");
	}

	public boolean activeButton() {
		if (students.size() > 0) {
			return true;
		}
		return false;
	}

	public String getUserName(Student item, String type) {
		String userName = "";
		SysUserDAO sysUserDAO = new SysUserDAO();
		switch (type) {
		case ContantsUtil.GroupUser.DEPARTMENT:
			if (item.getDepartmentStatus()) {
				userName = sysUserDAO.get(item.getDepartmentUserId()).getUserName();
			}
			break;
		case ContantsUtil.GroupUser.LIBRARY:
			if (item.getLibraryStatus()) {
				userName = sysUserDAO.get(item.getLibraryUserId()).getUserName();
			}
			break;
		case ContantsUtil.GroupUser.SCHOOLFEE:
			if (item.getSchoolFeeStatus()) {
				userName = sysUserDAO.get(item.getSchoolFeeUserId()).getUserName();
			}
			break;
		case ContantsUtil.GroupUser.BRIEF:
			if (item.getBriefStatus()) {
				userName = sysUserDAO.get(item.getBriefUserId()).getUserName();
			}
			break;
		case ContantsUtil.GroupUser.MCOM:
			if (item.getmComStatus()) {
				userName = sysUserDAO.get(item.getmComUserId()).getUserName();
			}
			break;
		default:
			break;
		}

		return userName;
	}

	public int countStudentbyGP(Long graduationPeriodId) {
		if (graduationPeriod.getGraduationPeriodId() != null) {
			int a = studentDAO.countStudentbyGP(graduationPeriod.getGraduationPeriodId());
			graduationPeriod.setNumStudent((long) a);
			graduationPeriodDAO.saveOrUpdate(graduationPeriod);
			return a;
		}
		return 0;
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

	public int countStudentsM() {
		if (graduationPeriod.getGraduationPeriodId() != null) {
			for (Student student : students) {
				if (student.getmComStatus() != null && student.getoComStatus() != null
						&& student.getmComStatus() == true && student.getoComStatus() == true) {
					int c = studentDAO.countStudentsM(graduationPeriod.getGraduationPeriodId(), true, true);
					return c;
				}
			}
		}
		return 0;
	}

	public int countStudentsO() {
		if (graduationPeriod.getGraduationPeriodId() != null) {
			for (Student student : students) {
				if (student.getoComStatus() != null && student.getoComStatus() == true) {
					int c = studentDAO.countStudentsO(graduationPeriod.getGraduationPeriodId(), true);
					return c;
				}
			}
		}
		return 0;
	}

	public int countStudentsB() {
		if (graduationPeriod.getGraduationPeriodId() != null) {
			for (Student student : students) {
				if (student.getBriefStatus() != null && student.getBriefStatus() == true) {
					int c = studentDAO.countStudentsB(graduationPeriod.getGraduationPeriodId(), true);
					return c;
				}
			}
		}
		return 0;
	}

	public void postProcessXLSTest(Object document) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		HSSFWorkbook wb = (HSSFWorkbook) document;
		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.shiftRows(0, sheet.getLastRowNum(), 1);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			Row row = sheet.getRow(0);
			row.setHeight((short) 550);
			Cell cell00 = row.createCell(0);
			cell00.setCellValue("Danh sách sinh viên tốt nghiệp " + " " + "tính đến" + dateFormat.format(toDate));
			CellStyle styleHeader = wb.createCellStyle();
			Font boldHeader = wb.createFont();
			boldHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldHeader.setFontHeightInPoints((short) 12);
			boldHeader.setFontName("Times New Roman");
			styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
			styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styleHeader.setFont(boldHeader);
			styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell00.setCellStyle(styleHeader);
			sheet.shiftRows(1, sheet.getLastRowNum(), 1);

			HSSFRow header = sheet.getRow(2);
			header.setHeight((short) 300);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
			Font bold = wb.createFont();
			bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
			bold.setFontHeightInPoints((short) 10);
			bold.setFontName("Times New Roman");
			cellStyle.setFont(bold);
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
			}
			for (int colNum = 0; colNum < header.getLastCellNum(); colNum++)
				wb.getSheetAt(0).autoSizeColumn(colNum);

			for (Row r : sheet) {
				for (int colNum = 0; colNum < r.getLastCellNum(); colNum++) {
//					wb.getSheetAt(0).autoSizeColumn(colNum);
					Cell cell = r.getCell(colNum);
					if (colNum == 4 || colNum == 5 || colNum == 6) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã HT");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa HT");
						}
					} else if (colNum == 7) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã KN");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa KN");
						}
					} else if (colNum == 8) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã chuyển");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa chuyển");
						}
					} else if (colNum == 9) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã trả");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa trả");
						}
					}

				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void postProcessXLS(Object document) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		HSSFWorkbook wb = (HSSFWorkbook) document;
		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.shiftRows(0, sheet.getLastRowNum(), 1);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
			Row row = sheet.getRow(0);
			row.setHeight((short) 550);
			Cell cell00 = row.createCell(0);
			cell00.setCellValue(
					"Danh sách sinh viên hoàn thủ tục tục ra trường " + graduationPeriod.getGraduationPeriodName()
							+ " (" + "tính đến ngày " + dateFormat.format(toDate) + ")");
			CellStyle styleHeader = wb.createCellStyle();
			Font boldHeader = wb.createFont();
			boldHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldHeader.setFontHeightInPoints((short) 12);
			boldHeader.setFontName("Times New Roman");
			styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
			styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styleHeader.setFont(boldHeader);
			styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell00.setCellStyle(styleHeader);
			sheet.shiftRows(1, sheet.getLastRowNum(), 1);

			HSSFRow header = sheet.getRow(2);
			header.setHeight((short) 300);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
			Font bold = wb.createFont();
			bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
			bold.setFontHeightInPoints((short) 12);
			bold.setFontName("Times New Roman");
			cellStyle.setFont(bold);
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
			}
			for (int colNum = 0; colNum < header.getLastCellNum(); colNum++)
				wb.getSheetAt(0).autoSizeColumn(colNum);

			for (Row r : sheet) {
				for (int colNum = 0; colNum < r.getLastCellNum(); colNum++) {
//					wb.getSheetAt(0).autoSizeColumn(colNum);
					Cell cell = r.getCell(colNum);
					if (colNum == 4) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã HT");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa HT");
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void postProcessXLSGP(Object document) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		HSSFWorkbook wb = (HSSFWorkbook) document;
		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.shiftRows(0, sheet.getLastRowNum(), 1);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
			Row row = sheet.getRow(0);
			row.setHeight((short) 550);
			Cell cell00 = row.createCell(0);
			cell00.setCellValue(
					"Danh sách sinh viên hoàn thủ tục tục ra trường " + graduationPeriod.getGraduationPeriodName()
							+ " (" + "tính đến ngày " + dateFormat.format(toDate) + ")");
			CellStyle styleHeader = wb.createCellStyle();
			Font boldHeader = wb.createFont();
			boldHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldHeader.setFontHeightInPoints((short) 12);
			boldHeader.setFontName("Times New Roman");
			styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
			styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styleHeader.setFont(boldHeader);
			styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell00.setCellStyle(styleHeader);
			sheet.shiftRows(1, sheet.getLastRowNum(), 1);

			HSSFRow header = sheet.getRow(2);
			header.setHeight((short) 300);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
			Font bold = wb.createFont();
			bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
			bold.setFontHeightInPoints((short) 12);
			bold.setFontName("Times New Roman");
			cellStyle.setFont(bold);
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
			}
			for (int colNum = 0; colNum < header.getLastCellNum(); colNum++){
				wb.getSheetAt(0).autoSizeColumn(colNum);}
			for (Row r : sheet) {
				for (int colNum = 0; colNum < r.getLastCellNum(); colNum++) {
//					wb.getSheetAt(0).autoSizeColumn(colNum);
					Cell cell = r.getCell(colNum);
					if (colNum == 4 || colNum == 5 || colNum == 6) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã HT");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa HT");
						}
					} else if (colNum == 7) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã KN");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa KN");
						}
					} else if (colNum == 8) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã chuyển");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa chuyển");
						}
					} else if (colNum == 9) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã trả");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa trả");
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void postProcessXLSD(Object document) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		HSSFWorkbook wb = (HSSFWorkbook) document;
		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.shiftRows(0, sheet.getLastRowNum(), 1);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			Row row = sheet.getRow(0);
			row.setHeight((short) 550);
			Cell cell00 = row.createCell(0);
			cell00.setCellValue(
					"Danh sách sinh viên hoàn thủ tục tục ra trường " + graduationPeriod.getGraduationPeriodName()
							+ " (" + "tính đến ngày " + dateFormat.format(toDate) + ")");
			CellStyle styleHeader = wb.createCellStyle();
			Font boldHeader = wb.createFont();
			boldHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldHeader.setFontHeightInPoints((short) 12);
			boldHeader.setFontName("Times New Roman");
			styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
			styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styleHeader.setFont(boldHeader);
			styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell00.setCellStyle(styleHeader);
			sheet.shiftRows(1, sheet.getLastRowNum(), 1);

			HSSFRow header = sheet.getRow(2);
			header.setHeight((short) 300);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
			Font bold = wb.createFont();
			bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
			bold.setFontHeightInPoints((short) 12);
			bold.setFontName("Times New Roman");
			cellStyle.setFont(bold);
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
			}
			for (int colNum = 0; colNum < header.getLastCellNum(); colNum++)
				wb.getSheetAt(0).autoSizeColumn(colNum);
			for (Row r : sheet) {
				for (int colNum = 0; colNum < r.getLastCellNum(); colNum++) {
//					wb.getSheetAt(0).autoSizeColumn(colNum);
					Cell cell = r.getCell(colNum);
					if (colNum == 4) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã trả");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa trả");
						}
					} else if (colNum == 5) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã HT");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa HT");
						}
					}

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void postProcessXLSO(Object document) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		HSSFWorkbook wb = (HSSFWorkbook) document;
		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.shiftRows(0, sheet.getLastRowNum(), 1);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
			Row row = sheet.getRow(0);
			row.setHeight((short) 550);
			Cell cell00 = row.createCell(0);
			cell00.setCellValue(
					"Danh sách sinh viên hoàn thủ tục tục ra trường " + graduationPeriod.getGraduationPeriodName()
							+ " (" + "tính đến ngày " + dateFormat.format(toDate) + ")");
			CellStyle styleHeader = wb.createCellStyle();
			Font boldHeader = wb.createFont();
			boldHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldHeader.setFontHeightInPoints((short) 12);
			boldHeader.setFontName("Times New Roman");
			styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
			styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styleHeader.setFont(boldHeader);
			styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell00.setCellStyle(styleHeader);
			sheet.shiftRows(1, sheet.getLastRowNum(), 1);

			HSSFRow header = sheet.getRow(2);
			header.setHeight((short) 300);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
			Font bold = wb.createFont();
			bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
			bold.setFontHeightInPoints((short) 12);
			bold.setFontName("Times New Roman");
			cellStyle.setFont(bold);
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
			}
			for (int colNum = 0; colNum < header.getLastCellNum(); colNum++)
				wb.getSheetAt(0).autoSizeColumn(colNum);
			for (Row r : sheet) {
				for (int colNum = 0; colNum < r.getLastCellNum(); colNum++) {
//					wb.getSheetAt(0).autoSizeColumn(colNum);
					Cell cell = r.getCell(colNum);
					if (colNum == 4) {
						if (cell.getStringCellValue().equalsIgnoreCase("true")) {
							cell.setCellValue("Đã chuyển");
						} else if (cell.getStringCellValue().equalsIgnoreCase("false")) {
							cell.setCellValue("Chưa chuyển");
						}
					} 
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void postProcessXLSStudentByGraNotComplete(Object document) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		HSSFWorkbook wb = (HSSFWorkbook) document;
		try {
			HSSFSheet sheet = wb.getSheetAt(0);
			sheet.shiftRows(0, sheet.getLastRowNum(), 1);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			Row row = sheet.getRow(0);
			row.setHeight((short) 550);
			Cell cell00 = row.createCell(0);
			cell00.setCellValue(
					"Danh sách sinh viên chưa upload bảng điểm tốt nghiệp đợt " + graduationPeriod.getGraduationPeriodName()
							+ " (" + "tính đến ngày " + dateFormat.format(toDate) + ")");
			CellStyle styleHeader = wb.createCellStyle();
			Font boldHeader = wb.createFont();
			boldHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
			boldHeader.setFontHeightInPoints((short) 12);
			boldHeader.setFontName("Times New Roman");
			styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
			styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			styleHeader.setFont(boldHeader);
			styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
			styleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cell00.setCellStyle(styleHeader);
			sheet.shiftRows(1, sheet.getLastRowNum(), 1);

			HSSFRow header = sheet.getRow(2);
			header.setHeight((short) 300);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			cellStyle.setWrapText(true);
			Font bold = wb.createFont();
			bold.setBoldweight(Font.BOLDWEIGHT_BOLD);
			bold.setFontHeightInPoints((short) 12);
			bold.setFontName("Times New Roman");
			cellStyle.setFont(bold);
			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
			}
			for (int colNum = 0; colNum < header.getLastCellNum(); colNum++)
				wb.getSheetAt(0).autoSizeColumn(colNum);
			for (Row r : sheet) {
				for (int colNum = 0; colNum < r.getLastCellNum(); colNum++) {
					Cell cell = r.getCell(colNum);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
		graduationPeriodDAO.saveGraPeriodAndST(graduationPeriod, students);
		// studentDAO.saveStudents(students);
		// studentDAO.saveOrUpdate(student);
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
			student.setDepartmentUserId(null);
		}
		if (student.getLibraryStatus() != null && student.getLibraryStatus() == true) {
			student.setLibraryUpdateTime(new Date());
			student.setLibraryUserId(SessionUtils.getUser().getId());
		} else {
			student.setLibraryUpdateTime(null);
			student.setLibraryUserId(null);
		}
		if (student.getSchoolFeeStatus() != null && student.getSchoolFeeStatus() == true) {
			student.setSchoolFeeUpdateTime(new Date());
			student.setSchoolFeeUserId(SessionUtils.getUser().getId());
		} else {
			student.setSchoolFeeUpdateTime(null);
			student.setSchoolFeeUserId(null);
		}
		if (student.getBriefStatus() != null && student.getBriefStatus() == true) {
			student.setBriefUpdateTime(new Date());
			student.setBriefUserId(SessionUtils.getUser().getId());
		} else {
			student.setBriefUpdateTime(null);
			student.setBriefUserId(null);
		}
		if (student.getoComStatus() != null && student.getmComStatus() != null && student.getmComStatus() == true
				&& student.getoComStatus() == true) {
			student.setmComUpdateTime(new Date());
			student.setmComUserId(SessionUtils.getUser().getId());
		} else {
			student.setmComUpdateTime(null);
			student.setmComUserId(null);
		}
		studentDAO.update(student);
		countStudentsSF();
		countStudentsD();
		countStudentsL();
		countStudentsB();
		countStudentsO();
	}

	public void autoUpdateDate(Student student, String type) {
		switch (type) {
		case ContantsUtil.GroupUser.DEPARTMENT:
			if (student.getDepartmentStatus() != null && student.getDepartmentStatus() == true) {
				student.setDepartmentUpdateTime(new Date());
				student.setDepartmentUserId(SessionUtils.getUser().getId());
			} else {
				student.setDepartmentUpdateTime(null);
				student.setDepartmentUserId(null);
			}
			countStudentsD();
			break;

		case ContantsUtil.GroupUser.LIBRARY:
			if (student.getLibraryStatus() != null && student.getLibraryStatus() == true) {
				student.setLibraryUpdateTime(new Date());
				student.setLibraryUserId(SessionUtils.getUser().getId());
			} else {
				student.setLibraryUpdateTime(null);
				student.setLibraryUserId(null);
			}
			countStudentsL();
			break;

		case ContantsUtil.GroupUser.SCHOOLFEE:
			if (student.getSchoolFeeStatus() != null && student.getSchoolFeeStatus() == true) {
				student.setSchoolFeeUpdateTime(new Date());
				student.setSchoolFeeUserId(SessionUtils.getUser().getId());
			} else {
				student.setSchoolFeeUpdateTime(null);
				student.setSchoolFeeUserId(null);
			}
			countStudentsSF();
			break;

		case ContantsUtil.GroupUser.BRIEF:
			if (student.getBriefStatus() != null && student.getBriefStatus() == true) {
				student.setBriefUpdateTime(new Date());
				student.setBriefUserId(SessionUtils.getUser().getId());
			} else {
				student.setBriefUpdateTime(null);
				student.setBriefUserId(null);
			}
			countStudentsB();
			break;

		case ContantsUtil.GroupUser.MCOM:
			if (student.getoComStatus() != null && student.getmComStatus() != null && student.getmComStatus() == true
					&& student.getoComStatus() == true) {
				student.setmComUpdateTime(new Date());
				student.setmComUserId(SessionUtils.getUser().getId());
			} else {
				student.setmComUpdateTime(null);
				student.setmComUserId(null);
			}
			countStudentsO();
			break;
		default:
			break;
		}
		studentDAO.update(student);
	}
	
	public void viewNumStudentF(){
		loadGraduatonPeriods();
		loadStudentFinishedByGP(graduationPeriod.getGraduationPeriodId());
		if (graduationPeriod  != null  && graduationPeriod.getGraduationPeriodId() != null) {
			if (loadStudentFinishedByGP(graduationPeriod.getGraduationPeriodId()).size() > 0) {
				int a = loadStudentFinishedByGP(graduationPeriod.getGraduationPeriodId()).size();
				if (a > 0) {
					graduationPeriod.setNumStudentF((long) a);
					graduationPeriodDAO.saveOrUpdate(graduationPeriod);
				}else {
					graduationPeriod.setNumStudentF(0L);
				}
			}
			RequestContext requestContext = RequestContext.getCurrentInstance();
			requestContext.update("form-gp-list:dtGP");
		}
	}

	public void cmdDeleteGP(GraduationPeriod graduationPeriod) {
		StudentDAO studentDAO = new StudentDAO();
		if (!studentDAO.checkGPInStudent(graduationPeriod.getGraduationPeriodId())) {
			graduationPeriodDAO.delete(graduationPeriod);
			graduationPeriods.remove(graduationPeriod);
			loadGraduatonPeriods();
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
					.findComponent("form-gp-list:dtGP");
			if (!dataTable.getFilters().isEmpty()) {
				dataTable.reset();// working
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.update("form-gp-list:dtGP");
			}
			super.showNotificationSuccsess();
		} else {
			this.showMessageWARN("common.summary.warning", super.readProperties("validate.fieldUseIn"));
		}

	}

	public void showDialogGP(GraduationPeriod graduationPeriod) {
		if (graduationPeriod == null) {
			this.graduationPeriod = new GraduationPeriod();
			this.graduationPeriod.setStartDate(new Date());
			this.graduationPeriod.setFinishDate(new Date());
			this.students.clear();
			this.isEdit = false;
		} else {
			this.isEdit = true;
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
		
//		if (student.getDepartmentStatus() && student.getLibraryStatus() && student.getSchoolFeeStatus()) {
//			if (student.getoComStatus()) {
//				if (student.getmComStatus()) {
//					System.out.println("Da hoan thanh thu tuc");
//				}else {
//					System.out.println("Chua hoan thanh thu tuc");
//				}
//			}else {
//				System.out.println("Da hoan thanh thu tuc");
//			}
//		}
//		
//		boolean is = student.getDepartmentStatus() && student.getLibraryStatus() && student.getSchoolFeeStatus() ? 
//				(student.getoComStatus() ? (student.getmComStatus() ? true : false) : true):false;

		return students;
	}

	// load list Student Finished by Graduation Period
	public List<Student> loadStudentFinishedByGP(long graduationPeriodId) {
		finishedStudents = new ArrayList<Student>();
		List<Student> finishedStudents1 = new ArrayList<Student>();
		List<Student> finishedStudents2 = new ArrayList<Student>();
		List<Student> finishedStudents3 = new ArrayList<Student>();

		finishedStudents1 = studentDAO.findSTFinishedByGPmF(graduationPeriodId);
		finishedStudents2 = studentDAO.findSTFinishedByGPmT(graduationPeriodId);
		finishedStudents3 = studentDAO.findSTFinishedByGPmFT(graduationPeriodId);

		if (finishedStudents1.size() > 0) {
			finishedStudents.addAll(finishedStudents1);
		}
		if (finishedStudents2.size() > 0) {
			finishedStudents.addAll(finishedStudents2);
		}
		if (finishedStudents3.size() > 0) {
			finishedStudents.addAll(finishedStudents3);
		}

		if (finishedStudents.size() > 0) {
			return finishedStudents;
		}
		return null;

	}

	// load list Student by Graduation Period and Ocom
	public List<Student> loadStudentByGPOCOM(long graduationPeriodId) {
		students = new ArrayList<Student>();
		students = studentDAO.findSTByGPOCOM(graduationPeriodId);
		return students;
	}

	public void checkTimeToDisableStatus(GraduationPeriod item) {
		if (item != null) {
			Date nowDate = new Date();

			int nowDateNum = getDateNum(nowDate);
			int startDateNum = getDateNum(item.getStartDate());
			int finishDateNum = getDateNum(item.getFinishDate());

			if (startDateNum <= nowDateNum && nowDateNum <= finishDateNum) {
				item.setStatus(true);
				this.readOnly = false;
			} else {
				this.readOnly = true;
				item.setStatus(false);
			}
			graduationPeriodDAO.saveOrUpdate(item);
		}
	}

	public Integer getDateNum(Date date) {
		int dateNum = 0;
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		dateNum = localDate.getYear() * 10000 + localDate.getMonthValue() * 100 + localDate.getDayOfMonth();

		return dateNum;
	}

	public void onRowSelect(SelectEvent event) {
		loadStudentByGP(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
		checkTimeToDisableStatus((GraduationPeriod) event.getObject());
		countStudentsSF();
		countStudentsD();
		countStudentsL();
		countStudentsB();
		countStudentsO();
		loadStudentByGraNotComplete(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
	}

	public void onRowUnselect(UnselectEvent event) {
		loadStudentByGP(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
	}

	public void onRowSelectOCOM(SelectEvent event) {
		loadStudentByGPOCOM(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
		checkTimeToDisableStatus((GraduationPeriod) event.getObject());
		countStudentsO();
		if (students.size() == 0) {
			this.showMessageWARN("common.summary.warning", super.readProperties("validate.sTOCom"));
		}
	}

	public void onRowUnselectOCOM(UnselectEvent event) {
		loadStudentByGPOCOM(((GraduationPeriod) event.getObject()).getGraduationPeriodId());
	}

	/***** DIALOG STUDENT *****/

	public void showDialogStudent(Student student) {
		if (student == null) {
			student = new Student();
			this.student = new Student();
			this.student.setDepartmentStatus(false);
			this.student.setLibraryStatus(false);
			this.student.setSchoolFeeStatus(false);
			this.student.setmComStatus(false);
			this.student.setBriefStatus(false);
			this.isEdit = false;
		} else {
			this.student = student;
			this.isEdit = true;
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgStudentWV').show();");

	}

	// public void cmdApplyDLGStudent() {
	// studentDAO.saveOrUpdate(student);
	// students.add(student);
	// RequestContext context = RequestContext.getCurrentInstance();
	// context.execute("PF('dlgStudentWV').hide();");
	//
	// }

	private boolean validateStudent() {
		boolean result = true;
		if (studentDAO.checkStudentId(student.getStudentId()) && !this.isEdit) {
			this.showMessageWARN("st.studentId", super.readProperties("validate.checkValueExist"));
			result = false;
		}
		return result;
	}

	public void cmdApplyDLGStudent() {

		if (validateStudent()) {
			student.setGraduationPeriodId(graduationPeriod.getGraduationPeriodId());
			studentDAO.saveOrUpdate(student);

			boolean checkNew = true;
			for (int i = 0; i < students.size(); i++) {
				if (student.getStudentId() == (students.get(i).getStudentId())) {
					students.set(i, student);
					checkNew = false;
					break;
				}
			}
			if (checkNew) {
				students.add(student);
				student.setDepartmentStatus(false);
				student.setLibraryStatus(false);
				student.setSchoolFeeStatus(false);
			}
			this.showMessageINFO("common.save", "Sinh viên");
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgStudentWV').hide();");
		}
	}

	public void showDialogStudentFinished(GraduationPeriod graduationPeriod) {
		if (graduationPeriod == null) {
			graduationPeriod = new GraduationPeriod();
		} else {
			this.graduationPeriod = graduationPeriod;
			loadStudentFinishedByGP(graduationPeriod.getGraduationPeriodId());
			if (finishedStudents.size() > 0) {
				RequestContext context = RequestContext.getCurrentInstance();
				context.execute("PF('dlgStudentFinnishedWV').show();");
			} else {
				this.showMessageWARN("", super.readProperties("validate.showDALStudentFinished")
						+ graduationPeriod.getGraduationPeriodName());
			}

		}

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
		GraduationScoreDAO gsDAO = new GraduationScoreDAO();
		if (!gsDAO.checkStudentIdInGraduationScore(student.getStudentId())) {
			studentDAO.delete(student);
			students.remove(student);
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
					.findComponent("form-gra-period-input:dtStudentImport");
			if (!dataTable.getFilters().isEmpty()) {
				dataTable.reset();// working
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.update("form-gra-period-input:dtStudentImport");
			}
			this.showMessageINFO("common.delete", "Sinh viên");
		} else {
			this.showMessageWARN("common.summary.warning", super.readProperties("validate.fieldUseIn"));
		}

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

	// Import Student

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
					String studentId = getCellValue(nextCell).toString();
					if (studentId.indexOf(".") != -1) {
						studentId = studentId.substring(0, studentId.indexOf("."));
					}
					if (Objects.nonNull(studentId) && !studentId.trim().isEmpty()) {
						student.setStudentId(studentId);
					}
					break;

				case 1:
					student.setStudentName((String) getCellValue(nextCell));
					break;
				case 2:
					student.set_class((String) getCellValue(nextCell));
					break;
				case 3:
					String oComStatus = getCellValue(nextCell).toString();
					if (Objects.nonNull(oComStatus) && !oComStatus.trim().isEmpty()) {
						if (oComStatus.trim().equalsIgnoreCase("x")) {
							student.setoComStatus(true);
						} else {
							student.setoComStatus(false);
						}
					} else {
						student.setoComStatus(false);
					}
					break;
				}
				// student.setGraduationPeriodId(graduationPeriod.getGraduationPeriodId());
				if (Objects.isNull(student.getoComStatus())) {
					student.setoComStatus(false);
				}
				student.setDepartmentStatus(false);
				student.setLibraryStatus(false);
				student.setSchoolFeeStatus(false);
				student.setmComStatus(false);
				student.setBriefStatus(false);
			}

			students.add(student);
		}

		workbook.close();
	}

	public Boolean checkIsExistST(String column, String value) {
		boolean result = false;
		if (column.equals("studentId")) {
			if (this.students.size() > 0) {
				for (Student s : this.students) {
					if (s.getStudentId().equals(value)) {
						result = true;
						break;
					}
				}
			}
			if (!result) {
				if (studentDAO.checkFieldIsExist(column, value, new Student())) {
					result = true;
				}
			}
		}
		return result;
	}

	public void saveGraPeriod() {
		if (validateGraPeriod()) {
			checkTimeToDisableStatus(graduationPeriod);
			if (!this.isEdit) {
				checkValidListST();
				// ExecutorService threadPool =
				// Executors.newFixedThreadPool(20);
				// CompletionService<Boolean> pool = new
				// ExecutorCompletionService<Boolean>(threadPool);
				// boolean errorImports = false;
				// graduationPeriodDAO.saveOrUpdate(graduationPeriod);
				// for(int i = 0; i < students.size(); i++){
				// pool.submit(new ImportStudentThread(graduationPeriod,
				// students.get(i), graduationPeriodDAO));
				// }
				// for(int i = 0; i < students.size(); i++){
				// try {
				// errorImports = pool.take().get();
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// } catch (ExecutionException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				// threadPool.shutdown();
				boolean errorImports = graduationPeriodDAO.saveGraPeriodAndST(graduationPeriod, students);
				if (errorImports) {
					this.showMessageWARN("",
							"Bạn đã import thành công" + " " + this.students.size() + " " + " bản ghi ");
					// this.showMessageWARN("Bạn đã import thành công ",
					// super.readProperties(" ") + this.students.size() + " " +
					// " bản ghi ");

				} else {
					this.showMessageINFO("Import lỗi : ", students.size() + " bản ghi");
				}

			} else {
				graduationPeriodDAO.update(graduationPeriod);
				super.showNotificationSuccsess();
			}
			countStudentbyGP(graduationPeriod.getGraduationPeriodId());
			checkTimeToDisableStatus(graduationPeriod);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgGPWV').hide();");
			loadGraduatonPeriods();
			countStudentsSF();
			countStudentsD();
			countStudentsL();
			countStudentsB();
			countStudentsO();
		}

	}

	// check name isExist
	public void checkValidListST() {
		List<String> stIDDuplicate = new ArrayList<>();
		List<String> stIDDuplicateInDb = new ArrayList<>();
		int count = 0;
		if (this.students.size() > 0) {
			List<String> lstStudentId = new ArrayList<>();
			int nullCountStudentId = 0;
			int duplicateCountStudentId = 0;

			for (int i = this.students.size() - 1; i >= 0; i--) {
				if (Objects.nonNull(this.students.get(i).getStudentId())) {
					lstStudentId.add(this.students.get(i).getStudentId());
				} else {
					this.students.remove(this.students.get(i));
					nullCountStudentId++;
				}
			}

			Set<String> uniqueSet = new HashSet<String>();
			for (String stId : lstStudentId) {
				if (uniqueSet.contains(stId)) {
					if (!stIDDuplicate.contains(stId)) {
						stIDDuplicate.add(stId);
					}
				} else {
					uniqueSet.add(stId);
				}
			}

			for (int i = this.students.size() - 1; i >= 0; i--) {
				if (stIDDuplicate.contains(this.students.get(i).getStudentId())) {
					this.students.remove(this.students.get(i));
					duplicateCountStudentId++;
				}
			}
			studentDAO = new StudentDAO();
			for (int i = this.students.size() - 1; i >= 0; i--) {
				if (studentDAO.checkStudentId(this.students.get(i).getStudentId())) {
					if (!stIDDuplicateInDb.contains(this.students.get(i).getStudentId())) {
						stIDDuplicateInDb.add(this.students.get(i).getStudentId());
						this.students.remove(this.students.get(i));
					}
				}
			}

			if (stIDDuplicate.size() > 0 || stIDDuplicateInDb.size() > 0 || nullCountStudentId > 0) {
				if (nullCountStudentId > 0)
					this.showMessageWARN("", "", "Số bản ghi null : " + nullCountStudentId
							+ "Số bản ghi bị trùng trên file:" + stIDDuplicate.size());
				if (stIDDuplicate.size() > 0)
					this.showMessageWARN("", "",
							"Số bản ghi bị trùng trên file:" + stIDDuplicate.size() + "\n" + stIDDuplicate.toString());
				if (stIDDuplicateInDb.size() > 0)
					this.showMessageWARN("", "", "Số bản ghi trùng trong csdl: " + stIDDuplicateInDb.size() + "\n "
							+ stIDDuplicateInDb.toString());

			}

		}
	}

	// Validate
	private boolean validateGraPeriod() {
		boolean result = true;
		if (graduationPeriod.getStartDate() != null && graduationPeriod.getFinishDate() != null
				&& graduationPeriod.getStartDate().getTime() > graduationPeriod.getFinishDate().getTime()) {
			this.showMessageWARN("", super.readProperties("validate.startDateBeforeFinishDate"));
			result = false;
		} else if (students.size() < 1 && !this.isEdit) {
			this.showMessageWARN("Bạn cần import dữ liệu sinh viên trước khi lưu lại", super.readProperties(""));
			result = false;
		}
		return result;
	}
	
	public List<Student> loadStudentByGraNotComplete(long graduationPeriodId) {
		lstStudentByGraNotComplete = studentDAO.findStudentByGraNotComplete(graduationPeriodId);
		return lstStudentByGraNotComplete;
	}

	private void loadGraduatonPeriods() {
		graduationPeriods = graduationPeriodDAO.findAllGP();
//		if (Objects.isNull(graduationPeriod)) {
//			loadStudentByGraNotComplete(this.graduationPeriod.getGraduationPeriodId());
//		}
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

	public List<Student> getSelectionStudents() {
		return selectionStudents;
	}

	public void setSelectionStudents(List<Student> selectionStudents) {
		this.selectionStudents = selectionStudents;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public List<Student> getLstImport() {
		return lstImport;
	}

	public void setLstImport(List<Student> lstImport) {
		this.lstImport = lstImport;
	}

	public List<Student> getLstStudentByGraNotComplete() {
		return lstStudentByGraNotComplete;
	}

	public void setLstStudentByGraNotComplete(List<Student> lstStudentByGraNotComplete) {
		this.lstStudentByGraNotComplete = lstStudentByGraNotComplete;
	}
	
}
