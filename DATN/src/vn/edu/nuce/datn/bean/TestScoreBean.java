package vn.edu.nuce.datn.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import vn.edu.nuce.datn.dao.SubjectDictionaryDAO;
import vn.edu.nuce.datn.dao.TestScoreDAO;
import vn.edu.nuce.datn.entity.GraduationScore;
import vn.edu.nuce.datn.entity.SubjectDictionary;
import vn.edu.nuce.datn.entity.TestScore;
import vn.edu.nuce.datn.util.ContantsUtil;
import vn.edu.nuce.datn.util.ResourceBundleUtil;
import vn.edu.nuce.datn.util.ValidateUtil;

@SuppressWarnings("serial")
@ManagedBean(name = "testScoreBean")
@ViewScoped
public class TestScoreBean extends BaseController implements Serializable {

	private TestScore testScore;
	private List<TestScore> testScores;
	private List<TestScore> lstTestScoreDLG;
	private List<TestScore> testScoresSelection;
	private TestScoreDAO testScoreDAO;

	private List<SubjectDictionary> lstSD;
	private Boolean isEdit;

	@PostConstruct
	public void init() {
		this.testScore = new TestScore();
		this.testScores = new ArrayList<TestScore>();
		this.testScoreDAO = new TestScoreDAO();
		this.lstTestScoreDLG = new ArrayList<TestScore>();
		loadTestScore();
		this.isEdit = false;
	}
	
	String srcPath = ResourceBundleUtil.getString("server.path.document");
	public void showDialogTS(TestScore testScore) {
		if (testScore == null) {
			this.testScore = new TestScore();
			lstTestScoreDLG = new ArrayList<TestScore>();
			this.isEdit = false;
		} else {
			this.testScore = testScore;
			this.isEdit = true;
			lstTestScoreDLG = new ArrayList<TestScore>();
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgTSWV').show();");
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

	/***** TEST SCORE *****/

	public String viewInfoSub(String subjectId, String type) {
		SubjectDictionaryDAO subjectDictionaryDAO = new SubjectDictionaryDAO();
		if (subjectId != null && subjectDictionaryDAO.checkSubjectId(subjectId)) {
			switch (type) {
			case ContantsUtil.InfoSub.SUB_NAME:
				return subjectDictionaryDAO.get(subjectId).getSubject();
			case ContantsUtil.InfoSub.CREDIT:
				return subjectDictionaryDAO.get(subjectId).getCredit().toString();
			default:
				break;
			}
		}
		return "";
	}

	// public Long getSubCredit(String subjectId) {
	// SubjectDictionaryDAO subjectDictionaryDAO = new SubjectDictionaryDAO();
	// return subjectDictionaryDAO.get(subjectId).getCredit();
	//
	// }

	public void saveTS() {
		testScoreDAO.saveTS(testScores);
		this.showMessageINFO("common.save", "Test Score");
	}

	// Validate
	private boolean validateTestScore() {
		boolean result = true;
		if (ValidateUtil.checkStringNullOrEmpty(this.testScore.getFileName())) {
			this.showMessageWARN("testScore", super.readProperties("validate.checkValueNameNull"));
			result = false;
		}
		if (testScoreDAO.checkFieldIsExist("fileName", testScore.getFileName(), this.testScore)) {
			this.showMessageWARN("testScore", super.readProperties("validate.checkValueNameExist"));
			result = false;
		}
		return result;
	}

	// DEL 1 Test Score
	// public Boolean cmdDeleteTS(TestScore testScore) {
	// boolean result;
	// if (testScore.getTestScoreId() != null) {
	// File file = new File(testScore.getFilePath());
	// if (file.exists()) {
	// if (file.delete()) {
	// testScoreDAO.delete(testScore);
	// if (testScoreDAO.get(testScore.getTestScoreId()) == null) {
	// testScores.remove(testScore);
	// result = true;
	// this.showMessageINFO("common.delete", "Test Score");
	// } else {
	// result = false;
	// System.out.println("xóa dữ liệu trên cơ sở dữ liệu
	// gặp lỗi");
	// }
	// } else {
	// result = false;
	// // System.out.println("xoa file k thành công");
	//
	// }
	// } else {
	// result = false;
	// System.out.println("file k tồn tại");
	// }
	// } else {
	// result = false;
	// System.out.println("id k tồn tại ");
	// }
	// return result;
	//
	// }

	public void cmdDeleteTS(TestScore testScore) {
		try {
			if (testScore.getTestScoreId() != null) {
				testScoreDAO.delete(testScore);
				testScores.remove(testScore);
//				File file = new File(testScore.getFilePath());
				File file = new File(srcPath + testScore.getFileName());
				file.delete();
				DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form-tc-list:dtTestScore");
				if (!dataTable.getFilters().isEmpty()) {
					dataTable.reset();// working
					RequestContext requestContext = RequestContext.getCurrentInstance();
					requestContext.update("form-tc-list:dtTestScore");
				}
				super.showNotificationSuccsess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.showNotificationFail();
		}
	}
	//
	// // DEL List Test Score
	// public void cmdRemoveTS() {
	// if (testScoresSelection.size() > 0) {
	// Integer delFail = 0;
	// Integer delSuccess = 0;
	// for (TestScore testScore : testScoresSelection) {
	// if (cmdDeleteTS(testScore)) {
	// testScores.remove(testScore);
	// delSuccess++;
	// System.out.println(delSuccess++);
	// } else {
	// delFail++;
	// System.out.println(delFail++);
	// }
	// }
	// testScoresSelection.clear();
	// activeButton();
	// loadTestScore();
	// this.showMessageINFO("validate.deleteSuccess", super.readProperties(""));
	// } else {
	// System.out.println("Fail");
	// }
	// }

	public void cmdRemoveTS() {
		try {
			if (testScoresSelection.size() > 0) {
				for (TestScore testScore : testScoresSelection) {
//					File file = new File(testScore.getFilePath());
					File file = new File(srcPath + testScore.getFileName());
					file.delete();
				}
				testScoreDAO.delListTestScore(testScoresSelection);
				testScoresSelection.clear();
				activeButton();
				loadTestScore();
				super.showNotificationSuccsess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.showNotificationFail();
		}

	}

	private void loadTestScore() {
		testScores = testScoreDAO.findAll();
	}

	// DownLoad File PDF
	public void downloadFileDemoSignature(TestScore testScore) {
		try {
//			String srcPath = testScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath + testScore.getFileName()));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath + testScore.getFileName()));
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
		try {
			String fileName = event.getFile().getFileName();
			// File file = new File(ResourceBundleUtil.getString("link") +
			// fileName);
			
			File file = new File(ResourceBundleUtil.getString("server.path.document") + fileName);
//			File file = new File(ResourceBundleUtil.getString("local.path.document") + fileName);


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

			if (testScore.getTestScoreId() != null) {
				testScores.remove(testScore);

//				File fileItem = new File(testScore.getFilePath());
				File fileItem = new File(srcPath + testScore.getFileName());
				fileItem.delete();
				testScores.remove(testScore);
				testScoreDAO.delete(testScore);

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
				testScore.setSubjectId(subjectId);
				testScore.setGroupId(groupId);
				testScore.setSchoolYear(schoolYear);
				testScore.setTest(test);
				testScore.setFilePath(file.getPath());
				testScore.setFileName(file.getName());

				testScoreDAO.save(testScore);
//				testScores.add(testScore);
				lstTestScoreDLG.add(testScore);
				loadTestScore();
				this.showNotificationSuccsess();
			} else {
				System.out.println("selectedTestScore is null");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Upload File PDF
	public void handleFileUpload(FileUploadEvent event) {

		try {
			String fileName = event.getFile().getFileName();
//			File file = new File(ResourceBundleUtil.getString("server.path.document.ex") + fileName);
			File file = new File(ResourceBundleUtil.getString("server.path.document") + fileName);
//			File file = new File(ResourceBundleUtil.getString("local.path.document") + fileName);

			InputStream inputStream = event.getFile().getInputstream();
			OutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();

			if (!file.exists()) {
				file.delete();
			}

			file.createNewFile();

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
			lstTestScoreDLG.add(testScore);
			testScores.add(testScore);
			loadTestScore();
			this.showNotificationSuccsess();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// View File PDF
	public String newTabFilePDF(TestScore testScore) {
		try {

//			String srcPath = testScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath + testScore.getFileName()));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath + testScore.getFileName()));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + testScore.getFileName() + "\"");
//			 FileOutputStream fos = new FileOutputStream(new
//			 File(ec.getRealPath(testScore.getFileName())));
//			 byte[] data = IOUtils.toByteArray(fis);
//			 fos.write(data, 0, data.length);
//			 fos.close();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
			response.sendRedirect(request.getContextPath() + ResourceBundleUtil.getString("link.document") + testScore.getFileName());
//			response.sendRedirect(request.getContextPath() + "/" + testScore.getFileName());
			return "";
		} catch (FileNotFoundException fnfex) {
			return "";
		} catch (IOException ioex) {
			return "";
		}
	}

	public boolean isStreamClosed(FileOutputStream out) {
		try {
			FileChannel fc = out.getChannel();
			return fc.position() >= 0L; // This may throw a
										// ClosedChannelException.
		} catch (java.nio.channels.ClosedChannelException cce) {
			return false;
		} catch (IOException e) {
		}
		return true;
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

	public List<TestScore> getLstTestScoreDLG() {
		return lstTestScoreDLG;
	}

	public void setLstTestScoreDLG(List<TestScore> lstTestScoreDLG) {
		this.lstTestScoreDLG = lstTestScoreDLG;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}
	

}
