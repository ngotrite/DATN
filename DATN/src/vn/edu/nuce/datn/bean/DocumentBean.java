package vn.edu.nuce.datn.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.poi.util.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import vn.edu.nuce.datn.dao.DocumentDAO;
import vn.edu.nuce.datn.entity.Document;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.TestScore;

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
	
	public void saveDoc() {
		documentDAO.saveOrUpdate(document);
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgDocWV').hide();");
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
				file.delete();
			}

			file.createNewFile();

			outputStream.flush();
			outputStream.close();
			
			document.setFilePath(file.getPath());
			document.setFileName(file.getName());
			
		} catch (IOException e) {
			e.printStackTrace();
			super.showNotificationFail();
		}
	}

	public void showDialogDoc(Document document) {
		if (document == null) {
			this.document = new Document();
		} else {
			this.document = document;
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('dlgDocWV').show();");
	}

	public void loadDocuments() {
		documents = documentDAO.findAll("issuanceDate");
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
