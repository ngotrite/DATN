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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import vn.edu.nuce.datn.dao.CertificateDAO;
import vn.edu.nuce.datn.entity.Certificate;

@SuppressWarnings("serial")
@ManagedBean(name = "certificateBean")
@ViewScoped
public class CertificateBean extends BaseController implements Serializable {
	private Certificate certificate;
	private CertificateDAO certificateDAO;
	private List<Certificate> lstcertificate;
	private UploadedFile file;
	
	@PostConstruct
	public void innit(){
		this.certificate = new Certificate();
		this.certificateDAO = new CertificateDAO();
		this.lstcertificate = new ArrayList<Certificate>();
		loadCer();
	}
	
	public void loadCer() {
		lstcertificate = certificateDAO.findAll();
	}
	
	public void saveCer() {
		certificateDAO.saveCertificate(lstcertificate);
		super.showNotificationSuccsess();
	}
	
	// Upload Certificate
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
					certificate.setStudentId((String) getCellValue(nextCell));
					break;
				case 1:
					certificate.setStudentName((String) getCellValue(nextCell));
					break;
				case 2:
					certificate.setBirthday((String) getCellValue(nextCell));
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
					certificate.setCertificateNo((String) getCellValue(nextCell));
					break;
				case 8:
					certificate.setGraduationYear((String) getCellValue(nextCell));
					break;
				case 9:
					certificate.setIssuanceDate((String) getCellValue(nextCell));
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
}
