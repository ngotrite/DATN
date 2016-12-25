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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import vn.edu.nuce.datn.dao.DocumentDAO;
import vn.edu.nuce.datn.entity.Document;
import vn.edu.nuce.datn.util.ResourceBundleUtil;
import vn.edu.nuce.datn.util.SessionUtils;

@SuppressWarnings("serial")
@ManagedBean(name = "documentBean")
@ViewScoped
public class DocumentBean extends BaseController implements Serializable {

	private List<Document> documents;
	private Document document;
	private DocumentDAO documentDAO;

	@PostConstruct
	public void init() {
		this.document = new Document();
		this.documentDAO = new DocumentDAO();
		this.documents = new ArrayList<Document>();
		loadDocuments();
	}

	public String getUserName(Document item) {
		String userName = "";
		userName = SessionUtils.getUserName();
		return userName;
	}

	// DEL 1 Doc
	public void deleteDoc(Document document) {
		try {
			if (document.getDocumentId() != null) {
				documentDAO.delete(document);
				documents.remove(document);
				File file = new File(document.getFilePath());
				file.delete();
				super.showNotificationSuccsess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			super.showNotificationFail();
		}
	}

	// DownLoad File PDF
	public void downloadFileDemoSignature(Document document) {
		try {
			String srcPath = document.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + document.getFileName() + "\"");
			OutputStream fileOut = ec.getResponseOutputStream();
			IOUtils.copy(fis, fileOut);
			fileOut.flush();
			fc.responseComplete();
		} catch (FileNotFoundException fnfex) {
		} catch (IOException ioex) {
		}
	}

	// View File PDF
	public String newTabFilePDF(Document document) {
		try {

			String srcPath = document.getFilePath();
			FileInputStream fis = new FileInputStream(new File(srcPath));
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			ec.responseReset();
			ec.setResponseContentType(ec.getMimeType(srcPath + document.getFileName()));
			// The Save As popup magic is done here. You can give it any file
			// name you want, this only won't work in MSIE, it will use current
			// request URL as file name instead.
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + document.getFileName() + "\"");
			FileOutputStream fos = new FileOutputStream(new File(ec.getRealPath(document.getFileName())));
			byte[] data = IOUtils.toByteArray(fis);
			fos.write(data, 0, data.length);
			fos.close();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
//			response.sendRedirect(request.getContextPath() + ResourceBundleUtil.getString("link.document") + document.getFileName());
			response.sendRedirect(request.getContextPath() + "/" + document.getFileName());
			document.setNumberDL(document.getNumberDL() + 1L);
			documentDAO.saveOrUpdate(document);
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

	public void saveDoc() {
		if (validateDoc()) {
			boolean checkNew = true;
			for (int i = 0; i < documents.size(); i++) {
				if (document.getDocumentId() == (documents.get(i).getDocumentId())) {
					documents.set(i, document);
					checkNew = false;
					break;
				}
			}
			if (checkNew) {
				documents.add(document);
				document.setUploadUserId(SessionUtils.getUser().getId());
			}
			documentDAO.saveOrUpdate(document);
			super.showNotificationSuccsess();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('dlgDocWV').hide();");
		}
	}

	public boolean validateDoc() {
		boolean result = true;
		// if (!documentDAO.checkName(document)) {
		// FacesContext context = FacesContext.getCurrentInstance();
		// context.addMessage(null,
		// new FacesMessage(FacesMessage.SEVERITY_WARN, "",
		// this.readProperties("common.alreadyExists")));
		// result = false;
		// }
		if (document.getFileName() == null || document.getFilePath() == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "", this.readProperties("validate.upLoad")));
			result = false;
		}
		return result;
	}

	// Upload File PDF
	public void handleFileUpload(FileUploadEvent event) {
		try {
			String fileName = event.getFile().getFileName();
			// E:\GitHub\DATN\DATN\WebContent\pdf
			// File file = new File("E:\\test\\" + fileName);

//			File file = new File(ResourceBundleUtil.getString("server.path.document") + fileName);
			File file = new File(ResourceBundleUtil.getString("local.path.document") + fileName);

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

			document.setFilePath(file.getPath());
			document.setFileName(file.getName());
			document.setNumberDL(0L);
			
			FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (IOException e) {
			e.printStackTrace();
			super.showNotificationFail();
		}
	}

	public void showDialogDoc(Document document) {
		if (document == null) {
			this.document = new Document();
			this.document.setUploadDate(new Date());
		} else {
			this.document = document;
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgDocWV').show();");
	}

	public void loadDocuments() {
		documents = documentDAO.findAllDoc();
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
}
