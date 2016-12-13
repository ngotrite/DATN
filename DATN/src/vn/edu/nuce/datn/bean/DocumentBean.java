package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import vn.edu.nuce.datn.dao.DocumentDAO;
import vn.edu.nuce.datn.entity.Document;

@SuppressWarnings("serial")
@ManagedBean(name = "documentBean")
@ViewScoped
public class DocumentBean extends BaseController implements Serializable{
	
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
