package vn.edu.nuce.datn.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.primefaces.event.FileUploadEvent;

import vn.edu.nuce.datn.dao.SubjectDictionaryDAO;
import vn.edu.nuce.datn.dao.TestScoreDAO;
import vn.edu.nuce.datn.entity.SubjectDictionary;
import vn.edu.nuce.datn.entity.TestScore;

@SuppressWarnings("serial")
@ManagedBean(name = "testScoreBean")
@ViewScoped
public class TestScoreBean extends BaseController implements Serializable {

	private TestScore testScore;
	private List<TestScore> testScores;
	private List<TestScore> testScoresSelection;
	private TestScoreDAO testScoreDAO;
	public int cout = 0;

	private List<SubjectDictionary> lstSD;

	@PostConstruct
	public void init() {
		this.testScore = new TestScore();
		this.testScores = new ArrayList<TestScore>();
		this.testScoreDAO = new TestScoreDAO();
		loadTestScore();
	}

	/***** SELECTION DELETE *****/

	public void selectEvent(AjaxBehaviorEvent event) {

	}

	public boolean activeButton() {
		if (testScoresSelection != null && testScoresSelection.size() > 0) {
			return true;
		}
		return false;
	}

	// DEL List Test Score
	public void cmdRemoveTS() {
		if (testScoresSelection.size() > 0) {
			for (TestScore testScore : testScoresSelection) {
				File file = new File(testScore.getFilePath());
				file.delete();
			}
			testScoreDAO.delListTestScore(testScoresSelection);
			testScoresSelection.clear();
			activeButton();
			loadTestScore();
			this.showMessageINFO("validate.deleteSuccess", super.readProperties(""));
		} else {
			System.out.println("Fail");
		}
	}


	/***** TEST SCORE *****/

	public String getSubjectName(String subjectId) {
		SubjectDictionaryDAO subjectDictionaryDAO = new SubjectDictionaryDAO();
		return subjectDictionaryDAO.get(subjectId).getSubject();

	}

	public Long getSubCredit(String subjectId) {
		SubjectDictionaryDAO subjectDictionaryDAO = new SubjectDictionaryDAO();
		return subjectDictionaryDAO.get(subjectId).getCredit();

	}

	public void saveTS() {
		testScoreDAO.saveTS(testScores);
		this.showMessageINFO("common.save", "Test Score");
	}

	// DEL 1 Test Score
	public void cmdDeleteTS(TestScore testScore) {
		if (testScore.getTestScoreId() != null) {
			testScoreDAO.delete(testScore);
			testScores.remove(testScore);
			File file = new File(testScore.getFilePath());
			file.delete();
			this.showMessageINFO("common.delete", "Test Score");
		} else {
			System.out.println("Fail");
		}

	}

	private void loadTestScore() {
		testScores = testScoreDAO.findAll();
	}

	// DownLoad File PDF
	public void downloadFileDemoSignature(TestScore testScore) {
		try {
			String srcPath = testScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + testScore.getFileName() + "\"");
			OutputStream fileOut = ec.getResponseOutputStream();
			IOUtils.copy(fis, fileOut);
			fileOut.flush();
			fc.responseComplete();
		} catch (FileNotFoundException fnfex) {
		} catch (IOException ioex) {
		}
	}

	private TestScore selectedTestScore;

	public void editFileItem(TestScore testScore) {
		selectedTestScore = testScore;
	}

	public void editFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		try {
			String fileName = event.getFile().getFileName();
			File file = new File("E:\\test\\" + fileName);

			InputStream inputStream = event.getFile().getInputstream();
			OutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();

			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}

			outputStream.flush();
			outputStream.close();

			if (selectedTestScore.getTestScoreId() != null) {
				File fileItem = new File(selectedTestScore.getFilePath());
				fileItem.delete();
				testScores.remove(selectedTestScore);
				testScoreDAO.delete(selectedTestScore);

				String[] values = fileName.substring(0, fileName.lastIndexOf(".")).split("_");

				String subjectId = "";
				String groupId = "";
				String schoolYear = "";
				String test = "";

				if (values.length >= 3) {
					subjectId = values[0];
					groupId = values[1];

					if (values[2].length() > 4) {
						schoolYear = values[2].substring(0, 4);
						test = values[2].substring(4, 5);
					}
				}
				selectedTestScore.setSubjectId(subjectId);
				selectedTestScore.setGroupId(groupId);
				selectedTestScore.setSchoolYear(schoolYear);
				selectedTestScore.setTest(test);
				selectedTestScore.setFilePath(file.getPath());
				selectedTestScore.setFileName(file.getName());

				testScoreDAO.save(selectedTestScore);
				testScores.add(selectedTestScore);

			} else {
				System.out.println("selectedTestScore is null");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Upload File PDF
	public void handleFileUpload(FileUploadEvent event) {
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, message);

		try {
			String fileName = event.getFile().getFileName();

			File file = new File("E:\\test\\" + fileName);

			InputStream inputStream = event.getFile().getInputstream();
			OutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();

			if (!file.exists()) {
				file.createNewFile();
			} else {
				file.delete();
				file.createNewFile();
			}

			outputStream.flush();
			outputStream.close();
			
				String[] values = fileName.substring(0, fileName.lastIndexOf(".")).split("_");

				String subjectId = "";
				String groupId = "";
				String schoolYear = "";
				String test = "";

				if (values.length >= 3) {
					subjectId = values[0];
					groupId = values[1];

					if (values[2].length() > 4) {
						schoolYear = values[2].substring(0, 4);
						test = values[2].substring(4, 5);
					}
				}
				TestScore testScore = new TestScore();
				testScore.setSubjectId(subjectId);
				testScore.setGroupId(groupId);
				testScore.setSchoolYear(schoolYear);
				testScore.setTest(test);
				testScore.setFilePath(file.getPath());
				testScore.setFileName(file.getName());
				testScoreDAO.save(testScore);
				testScores.add(testScore);

				cout++;
				System.out.println(cout + " File Success");
				
				
	
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	// View File PDF
	public String newTabFilePDF(TestScore testScore) {
		try {

			String srcPath = testScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath + testScore.getFileName()));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + testScore.getFileName() + "\"");
			FileOutputStream fos = new FileOutputStream(new File(ec.getRealPath(testScore.getFileName())));
			byte[] data = IOUtils.toByteArray(fis);
			fos.write(data, 0, data.length);
			fos.close();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.sendRedirect(request.getContextPath() + "/" + testScore.getFileName());
			return "";
		} catch (FileNotFoundException fnfex) {
			return "";
		} catch (IOException ioex) {
			return "";
		}
	}

	// **** GET SET ****//

	public TestScore getTestScore() {
		return testScore;
	}

	public void setTestScore(TestScore testScore) {
		this.testScore = testScore;
	}

	public List<TestScore> getTestScores() {
		return testScores;
	}

	public void setTestScores(List<TestScore> testScores) {
		this.testScores = testScores;
	}

	public TestScoreDAO getTestScoreDAO() {
		return testScoreDAO;
	}

	public void setTestScoreDAO(TestScoreDAO testScoreDAO) {
		this.testScoreDAO = testScoreDAO;
	}

	public List<TestScore> getTestScoresSelection() {
		return testScoresSelection;
	}

	public void setTestScoresSelection(List<TestScore> testScoresSelection) {
		this.testScoresSelection = testScoresSelection;
	}

	public List<SubjectDictionary> getLstSD() {
		return lstSD;
	}

	public void setLstSD(List<SubjectDictionary> lstSD) {
		this.lstSD = lstSD;
	}
}
