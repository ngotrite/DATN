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

import vn.edu.nuce.datn.dao.GraduationScoreDAO;
import vn.edu.nuce.datn.entity.GraduationScore;

@SuppressWarnings("serial")
@ManagedBean(name = "graScoreBean")
@ViewScoped
public class GraduationScoreBean extends BaseController implements Serializable {

	private GraduationScore graScore;
	private GraduationScoreDAO graScoreDAO;
	private List<GraduationScore> graScores;
	private List<GraduationScore> graScoresSelection;

	@PostConstruct
	public void init() {
		this.graScores = new ArrayList<GraduationScore>();
		this.graScore = new GraduationScore();
		this.graScoreDAO = new GraduationScoreDAO();
		loadGraScores();
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
				File file = new File(graScore.getFilePath());
				file.delete();
			}
			graScoreDAO.delListGS(graScoresSelection);
			graScoresSelection.clear();
			activeButton();
			loadGraScores();
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
		graScoreDAO.saveGS(graScores);
		this.showMessageINFO("common.save", "Graduation Score");
	}

	// DEL 1 Graduation Score
	public void cmdDeleteGS(GraduationScore graScore) {
		if (graScore.getStudentId()!= null) {
			graScoreDAO.delete(graScore);
			graScores.remove(graScore);
			File file = new File(graScore.getFilePath());
			file.delete();
			this.showMessageINFO("common.delete", "Test Score");
		} else {
			System.out.println("Fail");
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

			String studentId = fileName.substring(0, fileName.lastIndexOf("."));

			GraduationScore graScore = new GraduationScore();
			graScore.setStudentId(studentId);
			graScore.setFileName(file.getName());
			graScore.setFilePath(file.getPath());

			graScoreDAO.save(graScore);
			graScores.add(graScore);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// View File PDF
	public String newTabFilePDF(GraduationScore graScore) {
		try {

			String srcPath = graScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath + graScore.getFileName()));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + graScore.getFileName() + "\"");
			FileOutputStream fos = new FileOutputStream(new File(ec.getRealPath(graScore.getFileName())));
			byte[] data = IOUtils.toByteArray(fis);
			fos.write(data, 0, data.length);
			fos.close();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.sendRedirect(request.getContextPath() + "/" + graScore.getFileName());
			return "";
		} catch (FileNotFoundException fnfex) {
			return "";
		} catch (IOException ioex) {
			return "";
		}
	}

	private GraduationScore selectedGraScore;

	public void editFileItem(GraduationScore graScore) {
		selectedGraScore = graScore;
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

			if (selectedGraScore.getStudentId() != null) {
				File fileItem = new File(selectedGraScore.getFilePath());
				fileItem.delete();
				graScores.remove(selectedGraScore);
				graScoreDAO.delete(selectedGraScore);

				String studentId = fileName.substring(0, fileName.lastIndexOf("."));

				selectedGraScore.setStudentId(studentId);
				selectedGraScore.setFileName(file.getName());
				selectedGraScore.setFilePath(file.getPath());

				graScoreDAO.save(selectedGraScore);
				graScores.add(selectedGraScore);

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
			String srcPath = graScore.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath));
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


	
	//*** GET SET ***//
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

}
