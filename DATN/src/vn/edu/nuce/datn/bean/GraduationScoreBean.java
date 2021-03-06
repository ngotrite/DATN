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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

import vn.edu.nuce.datn.dao.GraduationPeriodDAO;
import vn.edu.nuce.datn.dao.GraduationScoreDAO;
import vn.edu.nuce.datn.dao.StudentDAO;
import vn.edu.nuce.datn.dao.SubjectDictionaryDAO;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.GraduationScore;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.util.ContantsUtil;
import vn.edu.nuce.datn.util.ResourceBundleUtil;
import vn.edu.nuce.datn.util.SessionUtils;

@SuppressWarnings("serial")
@ManagedBean(name = "graScoreBean")
@ViewScoped
public class GraduationScoreBean extends BaseController implements Serializable {

	private GraduationScore graScore;
	private GraduationScoreDAO graScoreDAO;
	private List<GraduationScore> graScores;
	private List<GraduationScore> lstgraScoresDLG;
	private List<GraduationScore> graScoresSelection;
	private Boolean isEdit;

	@PostConstruct
	public void init() {
		this.graScores = new ArrayList<GraduationScore>();
		this.graScore = new GraduationScore();
		this.graScoreDAO = new GraduationScoreDAO();
		this.lstgraScoresDLG = new ArrayList<GraduationScore>();
		loadGraScores();
		this.isEdit = false;
	}
	String srcPath = ResourceBundleUtil.getString("server.path.document");
//	String srcPath = ResourceBundleUtil.getString("local.path.document");
	public void showDialogGS(GraduationScore graScore) {
		if (graScore == null) {
			this.graScore = new GraduationScore();
			lstgraScoresDLG = new ArrayList<GraduationScore>();
			this.isEdit = false;
		}else {
			this.graScore = graScore;
			this.isEdit = true;
			lstgraScoresDLG = new ArrayList<GraduationScore>();
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgGSWV').show();");
	}

	public String viewInfoStu(GraduationScore item, String type) {
		StudentDAO studentDAO = new StudentDAO();
		// if (item.getStudentId() != null &&
		// studentDAO.checkStudentId(item.getStudentId())) {
		// return studentDAO.get(item.getStudentId()).get_class();
		// } else {
		// return "";
		// }
		if (item.getStudentId() != null && studentDAO.checkStudentId(item.getStudentId())) {
			switch (type) {
			case ContantsUtil.InfoStu.STUDENT_NAME:
				return studentDAO.get(item.getStudentId()).getStudentName();
			case ContantsUtil.InfoStu._CLASS:
				return studentDAO.get(item.getStudentId()).get_class();
			case ContantsUtil.InfoStu.GRAPERIOD_NAME:
				GraduationPeriodDAO gr = new GraduationPeriodDAO();
				Long a = studentDAO.get(item.getStudentId()).getGraduationPeriodId();
				if (a != null) {
					return gr.get(a).getGraduationPeriodName();
				}else {
					return "";
				}
			default:
				break;
			}
			return "";
		}
		return "";

	}

	/***** SELECTION DELETE *****/

	public void selectEvent(AjaxBehaviorEvent event) {

	}

	public boolean activeButton() {
		if (graScoresSelection != null && graScoresSelection.size() > 0) {
			return true;
		}
		return false;
	}

	// DEL List Test Score
	public void cmdRemoveGS() {
		if (graScoresSelection.size() > 0) {
			for (GraduationScore graScore : graScoresSelection) {
//				File file = new File(graScore.getFilePath());
				File file = new File(srcPath + graScore.getFileName());
				file.delete();
			}
			graScoreDAO.delListGS(graScoresSelection);
			graScoresSelection.clear();
			activeButton();
			loadGraScores();
			resetDataTable("form-gs-list:dtGraScore");
			this.showMessageINFO("validate.deleteSuccess", super.readProperties(""));
		} else {
			System.out.println("Fail");
		}
	}

	// Load DataTable GraduationScore
	public void loadGraScores() {
		graScores = graScoreDAO.findAll();
	}

	public void saveGS() {
		// graScoreDAO.saveGS(graScores);
		this.showMessageINFO("common.save", "Bảng điểm tốt nghiệp");
	}

	// DEL 1 Graduation Score
	public void cmdDeleteGS(GraduationScore graScore) {
		try {
			if (graScore.getStudentId() != null) {
				graScoreDAO.delete(graScore);
				graScores.remove(graScore);
//				File file = new File(graScore.getFilePath());
				File file = new File(srcPath + graScore.getFileName());
				file.delete();
				DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form-gs-list:dtGraScore");
				if (!dataTable.getFilters().isEmpty()) {
					dataTable.reset();// working
					RequestContext requestContext = RequestContext.getCurrentInstance();
					requestContext.update("form-gs-list:dtGraScore");
				}
				super.showNotificationSuccsess();
			}
		} catch (Exception e) {
			throw e;
		}

	}

	// Upload File PDF
	public void handleFileUpload(FileUploadEvent event) {
		try {
			String fileName = event.getFile().getFileName();

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

			String studentId = fileName.substring(0, fileName.lastIndexOf("."));

			GraduationScore graScore = new GraduationScore();
			graScore.setStudentId(studentId);
			graScore.setFileName(file.getName());
			graScore.setFilePath(file.getPath());
			
			// set mặc định khi upload thành công
			graScore.setStatus(true);
			graScore.setCreateDate(new Date());
			
			graScoreDAO.saveOrUpdate(graScore);
			lstgraScoresDLG.add(graScore);
			graScores.add(graScore);
			this.showNotificationSuccsess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void checkUploadStatus(){
		LocalDate now = LocalDate.now();
		String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		GraduationScoreDAO graduationScoreDAO = new GraduationScoreDAO();
		List<GraduationScore> lstGraduationScore = graduationScoreDAO.getLstCheckGraduationScore(dateStr);
		List<GraduationScore> lstUpdate = new ArrayList<>();
		for(GraduationScore item: lstGraduationScore){
//			File file = new File(ResourceBundleUtil.getString("local.path.document") + item.getFileName());
			File file = new File(ResourceBundleUtil.getString("server.path.document") + item.getFileName());
			if (!file.exists()) {
				item.setStatus(false);
				lstUpdate.add(item);
			}
		}
		if (lstUpdate.size() >= 0) {
			graduationScoreDAO.saveGS(lstUpdate);
		}
    }


	// View File PDF
	public String newTabFilePDF(GraduationScore graScore) {
		try {

//			String srcPath = graScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath + graScore.getFileName()));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath + graScore.getFileName()));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + graScore.getFileName() + "\"");
//			 FileOutputStream fos = new FileOutputStream(new
//			 File(ec.getRealPath(graScore.getFileName())));
//			 byte[] data = IOUtils.toByteArray(fis);
//			 fos.write(data, 0, data.length);
//			 fos.close();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.sendRedirect(request.getContextPath() + ResourceBundleUtil.getString("link.document") + graScore.getFileName());
//			response.sendRedirect(request.getContextPath() + "/" + graScore.getFileName());
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

	private GraduationScore selectedGraScore;

	public void editFileItem(GraduationScore graScore) {
		selectedGraScore = graScore;
	}

	public void editFileUpload(FileUploadEvent event) {
		try {
			String fileName = event.getFile().getFileName();
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

			if (graScore.getStudentId() != null) {
//				File fileItem = new File(graScore.getFilePath());
				File fileItem = new File(srcPath + graScore.getFileName());
				fileItem.delete();
				graScores.remove(graScore);
				graScoreDAO.delete(graScore);

				String studentId = fileName.substring(0, fileName.lastIndexOf("."));

				graScore.setStudentId(studentId);
				graScore.setFileName(file.getName());
				graScore.setFilePath(file.getPath());
				
				// set mặc định khi upload thành công
//				graScore.setStatus(true);
//				graScore.setCreateDate(new Date());

				graScoreDAO.saveOrUpdate(graScore);
//				graScores.add(graScore);
				lstgraScoresDLG.add(graScore);
				loadGraScores();
				FacesMessage message = new FacesMessage("Succesfull", event.getFile().getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);
			} else {
				System.out.println("selectedTestScore is null");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// DownLoad File PDF
	public void downloadFileDemoSignature(GraduationScore graScore) {
		try {
//			String srcPath = graScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath + graScore.getFileName()));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath + graScore.getFileName()));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + graScore.getFileName() + "\"");
			OutputStream fileOut = ec.getResponseOutputStream();
			IOUtils.copy(fis, fileOut);
			fileOut.flush();
			fc.responseComplete();
		} catch (FileNotFoundException fnfex) {
		} catch (IOException ioex) {
		}
	}

	// *** GET SET ***//
	public GraduationScore getGraScore() {
		return graScore;
	}

	public void setGraScore(GraduationScore graScore) {
		this.graScore = graScore;
	}

	public GraduationScoreDAO getGraScoreDAO() {
		return graScoreDAO;
	}

	public void setGraScoreDAO(GraduationScoreDAO graScoreDAO) {
		this.graScoreDAO = graScoreDAO;
	}

	public List<GraduationScore> getGraScores() {
		return graScores;
	}

	public void setGraScores(List<GraduationScore> graScores) {
		this.graScores = graScores;
	}

	public List<GraduationScore> getGraScoresSelection() {
		return graScoresSelection;
	}

	public void setGraScoresSelection(List<GraduationScore> graScoresSelection) {
		this.graScoresSelection = graScoresSelection;
	}

	public List<GraduationScore> getLstgraScoresDLG() {
		return lstgraScoresDLG;
	}

	public void setLstgraScoresDLG(List<GraduationScore> lstgraScoresDLG) {
		this.lstgraScoresDLG = lstgraScoresDLG;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

}
