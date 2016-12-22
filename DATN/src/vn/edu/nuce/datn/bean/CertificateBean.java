package vn.edu.nuce.datn.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.DateFormatConverter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import net.sf.jasperreports.engine.JasperReport;
import vn.edu.nuce.datn.dao.CertificateDAO;
import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.dao.MajorDAO;
import vn.edu.nuce.datn.entity.Certificate;
import vn.edu.nuce.datn.entity.Major;
import vn.edu.nuce.datn.entity.ReportResultDTO;
import vn.edu.nuce.datn.util.ContantsUtil;
import vn.edu.nuce.datn.util.ReportUtils;

@SuppressWarnings("serial")
@ManagedBean(name = "certificateBean")
@ViewScoped
public class CertificateBean extends BaseController implements Serializable {
	private Certificate certificate;
	private CertificateDAO certificateDAO;
	private List<Certificate> lstcertificate;
	private List<Certificate> lstCertificateHome;
	private UploadedFile file;

	private List<SelectItem> lstEducationSystem;
	private List<SelectItem> lstProgram;
	private List<SelectItem> lstGrade;
	private List<SelectItem> lstMajor;
	private List<Major> majors;
	private MajorDAO majorDAO;

	@PostConstruct
	public void innit() {
		this.certificate = new Certificate();
		this.certificateDAO = new CertificateDAO();
		this.lstcertificate = new ArrayList<Certificate>();
		this.majors = new ArrayList<Major>();
		this.majorDAO = new MajorDAO();
		loadCer();
		loadMajors();
	}

	public String getMajorName(Long majorId) {
		MajorDAO majorDAO = new MajorDAO();
		if (majorId != null) {
			return majorDAO.get(majorId).getMajorName();
		}
		return null;
	}

	public String createReportForGHBSError(String type, Long toYear) throws IOException {
		byte[] report = null;
		Map<String, Object> reportHeader = new HashMap<>();
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) ec.getRequest();
		this.majors = majorDAO.findAll();
		String reportName = "TKSLTN";

		String jasperPath = request.getSession().getServletContext().getRealPath("/jasper/" + reportName + ".jasper");
		String jrxmlPath = request.getSession().getServletContext().getRealPath("/jasper/" + reportName + ".jrxml");
		if (this.majors.size() > 0) {
			try {
				List<ReportResultDTO> resultList = new ArrayList<>();
				Long dataYear1 = 0l;
				Long dataYear2 = 0l;
				Long dataYear3 = 0l;
				Long dataYear4 = 0l;
				Long dataYear5 = 0l;
				for (Major major : this.majors) {
					resultList.add(new ReportResultDTO());
					resultList.get(resultList.size() - 1).setMajorName(major.getMajorName());

					dataYear1 = certificateDAO.countCer(String.valueOf(toYear - 4), major.getId());
					if (dataYear1 != null) {
						resultList.get(resultList.size() - 1).setIssDate1(dataYear1.toString());
					} else {
						resultList.get(resultList.size() - 1).setIssDate1("0");
					}

					dataYear2 = certificateDAO.countCer(String.valueOf(toYear - 3), major.getId());
					if (dataYear2 != null) {
						resultList.get(resultList.size() - 1).setIssDate2(dataYear2.toString());
					} else {
						resultList.get(resultList.size() - 1).setIssDate2("0");
					}

					dataYear3 = certificateDAO.countCer(String.valueOf(toYear - 2), major.getId());
					if (dataYear3 != null) {
						resultList.get(resultList.size() - 1).setIssDate3(dataYear3.toString());
					} else {
						resultList.get(resultList.size() - 1).setIssDate3("0");
					}

					dataYear4 = certificateDAO.countCer(String.valueOf(toYear - 1), major.getId());
					if (dataYear4 != null) {
						resultList.get(resultList.size() - 1).setIssDate4(dataYear4.toString());
					} else {
						resultList.get(resultList.size() - 1).setIssDate4("0");
					}

					dataYear5 = certificateDAO.countCer(String.valueOf(toYear), major.getId());
					if (dataYear5 != null) {
						resultList.get(resultList.size() - 1).setIssDate5(dataYear5.toString());
					} else {
						resultList.get(resultList.size() - 1).setIssDate5("0");
					}
				}

				Long totalDataYear1 = 0l;
				Long totalDataYear2 = 0l;
				Long totalDataYear3 = 0l;
				Long totalDataYear4 = 0l;
				Long totalDataYear5 = 0l;

				if (resultList.size() > 0) {
					for (int i = 0; i < resultList.size(); i++) {
						totalDataYear1 += Long.valueOf(resultList.get(i).getIssDate1());
						totalDataYear2 += Long.valueOf(resultList.get(i).getIssDate2());
						totalDataYear3 += Long.valueOf(resultList.get(i).getIssDate3());
						totalDataYear4 += Long.valueOf(resultList.get(i).getIssDate4());
						totalDataYear5 += Long.valueOf(resultList.get(i).getIssDate5());
					}
				}

				reportHeader.put("sum_nam1", String.valueOf(totalDataYear1));
				reportHeader.put("sum_nam2", String.valueOf(totalDataYear2));
				reportHeader.put("sum_nam3", String.valueOf(totalDataYear3));
				reportHeader.put("sum_nam4", String.valueOf(totalDataYear4));
				reportHeader.put("sum_nam5", String.valueOf(totalDataYear5));

				reportHeader.put("in_nam1", String.valueOf(toYear - 4));
				reportHeader.put("in_nam2", String.valueOf(toYear - 3));
				reportHeader.put("in_nam3", String.valueOf(toYear - 2));
				reportHeader.put("in_nam4", String.valueOf(toYear - 1));
				reportHeader.put("in_nam5", String.valueOf(toYear));

				JasperReport jasperReport = ReportUtils.getCompiledFile(jasperPath, jrxmlPath);

				if (type.equalsIgnoreCase("PDF")) {
					report = ReportUtils.generateReportPDF(resultList, reportHeader, jasperReport);
				} else if (type.equalsIgnoreCase("EXCEL")) {
					reportHeader.put("IS_IGNORE_PAGINATION", Boolean.TRUE);
					report = ReportUtils.generateReportExcel(reportHeader, jasperReport, resultList);
				}

				if (report != null) {
					try {
						String fileName = "ThongKeSoLieu.xls";
						ec.responseReset();
						ec.setResponseContentType("application/vnd.ms-excel");
						// The Save As popup magic is done here. You can give it
						// any file name you want, this only won't work in MSIE,
						// it will use current request URL as file name instead.
						ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
						try (OutputStream fileOut = ec.getResponseOutputStream()) {
							fileOut.write(report);
							fileOut.flush();
						}
						FacesContext.getCurrentInstance().responseComplete();
					} catch (FileNotFoundException ex) {
						ex.getMessage();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public void loadMajors() {
		majors = majorDAO.findAll();
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

	public void loadCerHome() {
		if (validateSearchCer()) {
			if (certificate.getStudentId() != null || certificate.getStudentName() != null
					|| certificate.getBirthday() != null || certificate.getCertificateNo() != null) {
				lstCertificateHome = certificateDAO.findCertificateHomeNew(certificate.getStudentId(),
						certificate.getStudentName(), certificate.getBirthday(), certificate.getCertificateNo());
			}
		}

	}

	public boolean validateSearchCer() {
		boolean result = true;
		if (certificate.getStudentId() == "" && certificate.getStudentName() == "" && certificate.getBirthday() == null
				&& certificate.getCertificateNo() == "") {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", this.readProperties("cer.checkNullAll")));
			result = false;
		}
		return result;
	}

	public void cmdDeleteCer(Certificate certificate) {
		certificateDAO.delete(certificate);
		lstcertificate.remove(certificate);
		super.showNotificationSuccsess();
	}

	public void cmdApplyDLGCer() {
		boolean checkNew = true;
		for (int i = 0; i < lstcertificate.size(); i++) {
			if (certificate.getStudentId() == (lstcertificate.get(i).getStudentId())) {
				lstcertificate.set(i, certificate);
				checkNew = false;
				break;
			}
		}
		if (checkNew) {
			lstcertificate.add(certificate);
		}
		certificate.setUpdateDate(new Date());
		certificateDAO.saveOrUpdate(certificate);
		super.showNotificationSuccsess();
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgCerWV').hide();");
		;
	}

	/*** load combo Education System ***/
	public List<SelectItem> loadEducationSystem() {
		lstEducationSystem = new ArrayList<SelectItem>();
		lstEducationSystem.add(new SelectItem(ContantsUtil.EducationSystem.DAI_HOC));
		lstEducationSystem.add(new SelectItem(ContantsUtil.EducationSystem.THAC_SY));
		lstEducationSystem.add(new SelectItem(ContantsUtil.EducationSystem.TIEN_SY));
		return lstEducationSystem;
	}

	/*** load combo Program ***/
	public List<SelectItem> loadProgram() {
		lstProgram = new ArrayList<SelectItem>();
		lstProgram.add(new SelectItem(ContantsUtil.Program.CHINH_QUY));
		lstProgram.add(new SelectItem(ContantsUtil.Program.LIEN_THONG));
		lstProgram.add(new SelectItem(ContantsUtil.Program.BANG_HAI));
		lstProgram.add(new SelectItem(ContantsUtil.Program.VUA_LAM_VUA_HOC));
		return lstProgram;
	}

	/*** load combo Grade ***/
	public List<SelectItem> loadGrade() {
		lstGrade = new ArrayList<SelectItem>();
		lstGrade.add(new SelectItem(ContantsUtil.Grade.TRUNG_BINH));
		lstGrade.add(new SelectItem(ContantsUtil.Grade.KHA));
		lstGrade.add(new SelectItem(ContantsUtil.Grade.GIOI));
		lstGrade.add(new SelectItem(ContantsUtil.Grade.XUAT_SAC));
		return lstGrade;
	}

	/*** load combo Major ***/
	public List<SelectItem> loadMajor() {
		lstMajor = new ArrayList<SelectItem>();
		lstMajor.add(new SelectItem(ContantsUtil.Major.KIEN_TRUC));
		lstMajor.add(new SelectItem(ContantsUtil.Major.QH_VUNG_VA_DO_THI));
		lstMajor.add(new SelectItem(ContantsUtil.Major.XD_DAN_DUNG_VA_CN));
		lstMajor.add(new SelectItem(ContantsUtil.Major.HT_KY_THUAT_TRONG_CONG_TRINH));
		lstMajor.add(new SelectItem(ContantsUtil.Major.XD_CANG_VA_DUONG_THUY));
		lstMajor.add(new SelectItem(ContantsUtil.Major.XD_THUY_LOI_VA_THUY_DIEN));
		lstMajor.add(new SelectItem(ContantsUtil.Major.TIN_HOC_XAY_DUNG));
		lstMajor.add(new SelectItem(ContantsUtil.Major.KT_XD_CONG_TRINH_GIAO_THONG));
		lstMajor.add(new SelectItem(ContantsUtil.Major.CAP_THOAT_NUOC));
		lstMajor.add(new SelectItem(ContantsUtil.Major.CN_KT_MOI_TRUONG));
		lstMajor.add(new SelectItem(ContantsUtil.Major.KT_CONG_TRINH_BIEN));
		lstMajor.add(new SelectItem(ContantsUtil.Major.CN_KT_VLXD));
		lstMajor.add(new SelectItem(ContantsUtil.Major.CONG_NGHE_THONG_TIN));
		lstMajor.add(new SelectItem(ContantsUtil.Major.MAY_XAY_DUNG));
		lstMajor.add(new SelectItem(ContantsUtil.Major.CO_GIOI_HOA_XD));
		lstMajor.add(new SelectItem(ContantsUtil.Major.KT_TRAC_DIA_VA_BAN_DO));
		lstMajor.add(new SelectItem(ContantsUtil.Major.KINH_TE_XAY_DUNG));
		lstMajor.add(new SelectItem(ContantsUtil.Major.KT_VA_QL_DO_THI));
		lstMajor.add(new SelectItem(ContantsUtil.Major.KT_VA_QL__BDS));
		return lstMajor;
	}

	/** show dialog Certificate Detail **/
	public void showDialogCer(Certificate certificate) {
		if (certificate == null) {
			certificate = new Certificate();
			certificate.setUpdateDate(new Date());
		}
		this.certificate = certificate;
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgCerWV').show();");
	}

	public void loadCer() {
		lstcertificate = certificateDAO.findAll();
	}

	public void saveCer() {
		certificateDAO.saveCertificate(lstcertificate);
		super.showNotificationSuccsess();
	}

	/** Upload Certificate **/
	public void uploadExel(FileUploadEvent event) throws IOException {
		file = event.getFile();

		try {
			readExcelFile(file);
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

	// Import Certificate
	public void readExcelFile(UploadedFile file) throws IOException {
		Workbook workbook = new HSSFWorkbook(file.getInputstream());

		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			if (nextRow.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			Certificate certificate = new Certificate();
			while (cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				int columnIndex = nextCell.getColumnIndex();

				switch (columnIndex) {
				case 0:
					String studentId = getCellValue(nextCell).toString();
					if (studentId.indexOf(".") != -1) {
						studentId = studentId.substring(0, studentId.indexOf("."));
					}
					certificate.setStudentId(studentId);
					break;
				case 1:
					certificate.setStudentName((String) getCellValue(nextCell));
					break;
				case 2:
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date updateDate = new Date();
					try {
						updateDate = dateFormat.parse(getCellValue(nextCell));
						certificate.setBirthday(updateDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 3:
					certificate.setBirthPlace((String) getCellValue(nextCell));
					break;
				case 4:
					certificate.setEducationSystem((String) getCellValue(nextCell));
					break;
				case 5:
					certificate.setProgram((String) getCellValue(nextCell));
					break;
				case 6:
					certificate.setMajor((String) getCellValue(nextCell));
					break;
				case 7:
					String certificateNo = getCellValue(nextCell).toString();
					if (certificateNo.indexOf(".") != -1) {
						certificateNo = certificateNo.substring(0, certificateNo.indexOf("."));
					}
					certificate.setCertificateNo(certificateNo);
					break;
				case 8:
					String graduationYear = getCellValue(nextCell).toString();
					if (graduationYear.indexOf(".") != -1) {
						graduationYear = graduationYear.substring(0, graduationYear.indexOf("."));
					}
					certificate.setGraduationYear(graduationYear);
					// certificate.setGraduationYear((String)
					// getCellValue(nextCell));
					break;
				case 9:
					// certificate.setIssuanceDate((String)
					// getCellValue(nextCell));
					SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
					Date updateDate1 = new Date();
					try {
						updateDate1 = dateFormat1.parse(getCellValue(nextCell));
						certificate.setIssuanceDate(updateDate1);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 10:
					certificate.setGrade((String) getCellValue(nextCell));
					break;
				}
				certificate.setUpdateDate(new Date());
			}
			lstcertificate.add(certificate);
		}
		workbook.close();
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public List<Certificate> getLstcertificate() {
		return lstcertificate;
	}

	public void setLstcertificate(List<Certificate> lstcertificate) {
		this.lstcertificate = lstcertificate;
	}

	public List<Certificate> getLstCertificateHome() {
		return lstCertificateHome;
	}

	public void setLstCertificateHome(List<Certificate> lstCertificateHome) {
		this.lstCertificateHome = lstCertificateHome;
	}

	public List<Major> getMajors() {
		return majors;
	}

	public void setMajors(List<Major> majors) {
		this.majors = majors;
	}

}
