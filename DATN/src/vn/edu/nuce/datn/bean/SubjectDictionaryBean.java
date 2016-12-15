
package vn.edu.nuce.datn.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


import vn.edu.nuce.datn.dao.SubjectDictionaryDAO;
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

	@PostConstruct
	public void init() {
		this.subjectDictionary =new SubjectDictionary();
		this.subjectDictionaryDAO = new SubjectDictionaryDAO();
		this.subjectDictionaries = new ArrayList<SubjectDictionary>();
		this.subjectDictionariesSelection = new ArrayList<SubjectDictionary>();
		loadSubjectDictionary();

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
		subjectDictionaryDAO.delete(subjectDictionary);
		subjectDictionaries.remove(subjectDictionary);
		this.showMessageINFO("common.delete", "Subject Dictionary");
	}

	/***** DIALOG SUBJECT DICTONARY *****/

	public void showDialogSD(SubjectDictionary subjectDictionary) {
		if (subjectDictionary == null) {
			subjectDictionary = new SubjectDictionary();
		}
		this.subjectDictionary = subjectDictionary;

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgSubjectDictionaryWV').show();");

	}

	public void cmdApplyDLGSD() {
		if (!subjectDictionaries.contains(subjectDictionary)) {
			subjectDictionaries.add(subjectDictionary);

		} else {
			super.showNotificationFail();
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgSubjectDictionaryWV').hide();");

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

				this.showMessageWARN("Subject Dictionary",
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
						if (checkIsExistSD("subjectId", (String) getCellValue(nextCell))) {
							codeDuplicate.add((String) getCellValue(nextCell));
							hasError = true;
						} else {
							subjectDictionary.setSubjectId((String) getCellValue(nextCell));
							hasError = false;
						}
						break;
					case 1:
						subjectDictionary.setSubject((String) getCellValue(nextCell));
						hasError = false;

						break;
					case 2:
						subjectDictionary.setCredit((Double.valueOf(getCellValue(nextCell))).longValue());
						hasError = false;
						break;

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
			this.showMessageWARN("Subject Code",
					super.readProperties("your inport file") + " has " + count + " success record and "
							+ codeDuplicate.size() + " duplicate record with name : " + listCodeDuplicate);
		}

		workbook.close();
	}

	public Boolean checkIsExistSD(String column, String value) {
		boolean result = false;

		if (column.equals("SUBJECT_ID")) {
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

}
