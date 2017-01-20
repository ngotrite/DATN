
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
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import vn.edu.nuce.datn.dao.SubjectDictionaryDAO;
import vn.edu.nuce.datn.dao.TestScoreDAO;
import vn.edu.nuce.datn.entity.GraduationScore;
import vn.edu.nuce.datn.entity.SubjectDictionary;
import vn.edu.nuce.datn.util.ValidateUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "subjectDictionaryBean")
@ViewScoped
public class SubjectDictionaryBean extends BaseController implements Serializable {

	private SubjectDictionary subjectDictionary;
	private List<SubjectDictionary> subjectDictionaries;
	private SubjectDictionaryDAO subjectDictionaryDAO;
	private UploadedFile file;
	private List<SubjectDictionary> subjectDictionariesSelection;
	private List<SubjectDictionary> lstSD;
	public Boolean isEdit;

	@PostConstruct
	public void init() {
		this.subjectDictionary = new SubjectDictionary();
		this.subjectDictionaryDAO = new SubjectDictionaryDAO();
		this.subjectDictionaries = new ArrayList<SubjectDictionary>();
		this.subjectDictionariesSelection = new ArrayList<SubjectDictionary>();
		this.lstSD = new ArrayList<SubjectDictionary>();
		loadSubjectDictionary();
		this.isEdit = false;

	}
	
	
	
	public void showDialogImport(SubjectDictionary subjectDictionary) {
		if (subjectDictionary == null) {
			this.subjectDictionary = new SubjectDictionary();
			subjectDictionaries = new ArrayList<SubjectDictionary>();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgSDWV').show();");
		}else {
			//Do nothing
		}
	}
	
	public void saveSDImport() {
			subjectDictionaryDAO.saveSubjectDictionary(subjectDictionaries);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgSDWV').hide();");
			super.showNotificationSuccsess();
	}

	/***** SELECTION DELETE *****/

	public void selectEvent(AjaxBehaviorEvent event) {

	}

	public boolean activeButton() {
		if (subjectDictionariesSelection != null && subjectDictionariesSelection.size() > 0) {
			return true;
		}
		return false;
	}

	public void commandRemoveSD() {
		if (subjectDictionaryDAO.delListSubjectDictionary(subjectDictionariesSelection)) {
			subjectDictionariesSelection.clear();
			activeButton();
			loadSubjectDictionary();
			this.showMessageINFO("validate.deleteSuccess", super.readProperties(""));
		}
	}

	/***** SUBJECT DICTONARY *****/

	private void loadSubjectDictionary() {
		subjectDictionaries = subjectDictionaryDAO.findAll();
	}

	public void saveSubjectDictionary() {
		if (checkValidSubjectDictionares()) {
			subjectDictionaryDAO.saveSubjectDictionary(subjectDictionaries);
			super.showNotificationSuccsess();
		}

	}

	public void cmdDeleteSD(SubjectDictionary subjectDictionary) {
		TestScoreDAO tsDAO = new TestScoreDAO();
		if (!tsDAO.checkSubjectIdInTestScore(subjectDictionary.getSubjectId())) {
			subjectDictionaryDAO.delete(subjectDictionary);
			subjectDictionaries.remove(subjectDictionary);
			DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
					.findComponent("form-sd-list:dtSubjectDictionary");
			if (!dataTable.getFilters().isEmpty()) {
				dataTable.reset();// working
				RequestContext requestContext = RequestContext.getCurrentInstance();
				requestContext.update("form-sd-list:dtSubjectDictionary");
			}
			this.showMessageINFO("common.delete", "Từ điển môn học");
			
		} else {
			this.showMessageWARN("common.summary.warning", super.readProperties("validate.fieldUseIn"));
		}

	}

	/***** DIALOG SUBJECT DICTONARY *****/

	public void showDialogSD(SubjectDictionary subjectDictionary) {
		if (subjectDictionary == null) {
			subjectDictionary = new SubjectDictionary();
			this.isEdit = false;
		}else {
			this.isEdit = true;
		}
		this.subjectDictionary = subjectDictionary;

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgSubjectDictionaryWV').show();");

	}

	public void cmdApplyDLGSD() {
//		if (!subjectDictionaries.contains(subjectDictionary)) {
//			subjectDictionaries.add(subjectDictionary);
//
//		} else {
//		}
//		RequestContext context = RequestContext.getCurrentInstance();
//		context.execute("PF('dlgSubjectDictionaryWV').hide();");
		
		if (validateSub()) {
			boolean checkNew = true;
			for (int i = 0; i < subjectDictionaries.size(); i++) {
				if (subjectDictionary.getSubjectId() == (subjectDictionaries.get(i).getSubjectId())) {
					subjectDictionaries.set(i, subjectDictionary);
					checkNew = false;
					break;
				}
			}
			if (checkNew) {
				subjectDictionaries.add(subjectDictionary);
			}
			subjectDictionaryDAO.saveOrUpdate(subjectDictionary);
			super.showNotificationSuccsess();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgSubjectDictionaryWV').hide();");
		}

	}
	
	private boolean validateSub() {
		boolean result = true;
		if (subjectDictionaryDAO.checkSubjectId(subjectDictionary.getSubjectId()) && !this.isEdit) {
			this.showMessageWARN("st.studentId", super.readProperties("validate.checkValueExist"));
			result = false;
		}
		return result;
	}

	// Check name isExist
	public Boolean checkValidSubjectDictionares() {
		boolean result = true;

		if (this.subjectDictionaries.size() > 0) {
			List<String> codeDuplicate = new ArrayList<>();
			for (int i = 0; i < this.subjectDictionaries.size(); i++) {
				for (int j = 0; j < this.subjectDictionaries.size(); j++) {
					if (i != j) {
						if (!ValidateUtil.checkStringNullOrEmpty(this.subjectDictionaries.get(i).getSubjectId())) {
							if (this.subjectDictionaries.get(i).getSubjectId()
									.equals(this.subjectDictionaries.get(j).getSubjectId())) {
								codeDuplicate.add(this.subjectDictionaries.get(i).getSubjectId());
								result = false;
							}
						}
					}
				}
			}

			if (codeDuplicate.size() > 0) {
				String listCodeDuplicate = "";
				for (int i = 0; i < codeDuplicate.size(); i++) {
					if (listCodeDuplicate.isEmpty()) {
						listCodeDuplicate += codeDuplicate.get(i);
					} else {
						if (listCodeDuplicate.indexOf(codeDuplicate.get(i)) == -1)
							listCodeDuplicate += "," + codeDuplicate.get(i);
					}
				}

				this.showMessageWARN("Môn học",
						super.readProperties("validate.checkValueNameExist") + " " + listCodeDuplicate);
			}
		}
		return result;
	}

	// Upload Subject Dictionary
	public void uploadExel(FileUploadEvent event) throws IOException {
		file = event.getFile();

		try {
			readZoneFromExcelFile(file);
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

	public void readZoneFromExcelFile(UploadedFile file) throws IOException {
		Workbook workbook = new HSSFWorkbook(file.getInputstream());
		Sheet firstSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = firstSheet.iterator();
		List<String> codeDuplicate = new ArrayList<>();
		int count = 0;
		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			if (nextRow.getRowNum() == 0) {
				continue;
			}
			Iterator<Cell> cellIterator = nextRow.cellIterator();
			SubjectDictionary subjectDictionary = new SubjectDictionary();
			boolean hasError = false;
			if (!hasError) {
				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {
					case 0:
						String subjectId = getCellValue(nextCell).toString();
						if (checkIsExistSD("subjectId", subjectId)) {
							codeDuplicate.add(subjectId);
							hasError = true;
						} else {
							
							if (subjectId != null && !subjectDictionaryDAO.checkSubjectId(subjectId)) {
								subjectDictionary.setSubjectId(subjectId);
								hasError = false;
							} else {
								codeDuplicate.add(subjectId);
								hasError = true;
							}
							
						}
						break;
					case 1:
						if (hasError == false) {
							subjectDictionary.setSubject((String) getCellValue(nextCell));
							break;
						}
					case 2:
						if (hasError == false) {
							subjectDictionary.setCredit((Double.valueOf(getCellValue(nextCell))).longValue());
							break;
						}
					}
				}
			}
			if (!hasError) {
				subjectDictionaries.add(subjectDictionary);
				count++;
			}

		}
		if (codeDuplicate.size() > 0) {
			String listCodeDuplicate = "";
			for (int i = 0; i < codeDuplicate.size(); i++) {
				if (listCodeDuplicate.isEmpty()) {
					listCodeDuplicate += codeDuplicate.get(i);
				} else {
					listCodeDuplicate += "," + codeDuplicate.get(i);
				}
			}
//			this.showMessageWARN("Mã môn học",
//					super.readProperties("your import file") + " has " + count + " success record and "
//							+ codeDuplicate.size() + " duplicate record with name : " + listCodeDuplicate);
			this.showMessageWARN("Bạn đã import thành công ",
					super.readProperties(" ") + count + " bản ghi và "
							+ codeDuplicate.size() + "bản ghi bị trùng : " + listCodeDuplicate);
		}else {
			this.showMessageINFO("Bạn đã import thành công : ", count  + "môn học");
		}
		workbook.close();
	}

	public Boolean checkIsExistSD(String column, String value) {
		boolean result = false;
		if (column.equals("subjectId")) {
			if (this.subjectDictionaries.size() > 0) {
				for (SubjectDictionary s : this.subjectDictionaries) {
					if (s.getSubjectId().equals(value)) {
						result = true;
						break;
					}
				}
			}

			if (!result) {
				if (subjectDictionaryDAO.checkFieldIsExist(column, value, new SubjectDictionary())) {
					result = true;
				}
			}
		}
		return result;
	}

	// ***** GET SET ****//
	public List<SubjectDictionary> getSubjectDictionaries() {
		return subjectDictionaries;
	}

	public void setSubjectDictionaries(List<SubjectDictionary> subjectDictionaries) {
		this.subjectDictionaries = subjectDictionaries;
	}

	public SubjectDictionaryDAO getSubjectDictionaryDAO() {
		return subjectDictionaryDAO;
	}

	public void setSubjectDictionaryDAO(SubjectDictionaryDAO subjectDictionaryDAO) {
		this.subjectDictionaryDAO = subjectDictionaryDAO;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public SubjectDictionary getSubjectDictionary() {
		return subjectDictionary;
	}

	public void setSubjectDictionary(SubjectDictionary subjectDictionary) {
		this.subjectDictionary = subjectDictionary;
	}

	public List<SubjectDictionary> getSubjectDictionariesSelection() {
		return subjectDictionariesSelection;
	}

	public void setSubjectDictionariesSelection(List<SubjectDictionary> subjectDictionariesSelection) {
		this.subjectDictionariesSelection = subjectDictionariesSelection;
	}

	public List<SubjectDictionary> getLstSD() {
		return lstSD;
	}

	public void setLstSD(List<SubjectDictionary> lstSD) {
		this.lstSD = lstSD;
	}

}
