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
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

import vn.edu.nuce.datn.dao.DocumentDAO;
import vn.edu.nuce.datn.dao.SysUserDAO;
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
	private List<Document> filteredDocuments;
	private Boolean isEdit;
	private FileUploadEvent eventUpload;
	private Boolean isUpload;

	@PostConstruct
	public void init() {
		this.isUpload = false;
		this.isEdit = false;
		this.eventUpload = null; 
		this.document = new Document();
		this.documentDAO = new DocumentDAO();
		this.documents = new ArrayList<Document>();
//		this.filteredDocuments = new ArrayList<Document>();
		loadDocuments();
	}
	

	public void removeAll() {
		for (Document document : documents) {
			File file = new File(document.getFilePath());
			file.delete();
		}
		documentDAO.delListDoc(documents);
		documents.clear();
		super.showNotificationSuccsess();
	}
	


	public String getUserName(Document item) {
		String userName = "";
		SysUserDAO sysUserDAO = new SysUserDAO();
		userName = sysUserDAO.get(item.getUploadUserId()).getUserName();
//		userName = SessionUtils.getUserName();
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
				DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form-doc-list:dtDocument");
				if (!dataTable.getFilters().isEmpty()) {
					dataTable.reset();// working
					RequestContext requestContext = RequestContext.getCurrentInstance();
					requestContext.update("form-doc-list:dtDocument");
				}
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
//			FileOutputStream fos = new FileOutputStream(new File(ec.getRealPath(document.getFileName())));
//			byte[] data = IOUtils.toByteArray(fis);
//			fos.write(data, 0, data.length);
//			fos.close();
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			response.sendRedirect(request.getContextPath() + ResourceBundleUtil.getString("link.document") + document.getFileName());
//			response.sendRedirect(request.getContextPath() + "/" + document.getFileName());
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
	
	public void delFile() {
		File file = new File(ResourceBundleUtil.getString("server.path.document") + document.getFileName());
//		File file = new File(ResourceBundleUtil.getString("local.path.document") + document.getFileName());
		file.delete();
		document.setFileName(null);
		this.isUpload = true;
		RequestContext.getCurrentInstance().update("form-dlg-doc:txtFileName");
		RequestContext.getCurrentInstance().update("form-dlg-doc:btnUploadDoc");
		this.showMessageWARN("", "", "Bạn đã delete file thành công, vui lòng chọn (Choose) file mới trước khi lưu");
		
	}

	// Upload File PDF
	public void handleFileUpload(FileUploadEvent event) {
		try {
			String fileName = event.getFile().getFileName();
			// E:\GitHub\DATN\DATN\WebContent\pdf
			// File file = new File("E:\\test\\" + fileName);

			File file = new File(ResourceBundleUtil.getString("server.path.document") + fileName);
//			File file = new File(ResourceBundleUtil.getString("local.path.document") + fileName);
			
			String newFileName = generateFileName(fileName);
			if(!this.isEdit){
				if (file.exists()) {
//					file = new File(ResourceBundleUtil.getString("local.path.document") + newFileName);
					file = new File(ResourceBundleUtil.getString("server.path.document") + newFileName);
				}
			} else {
//				if (file.exists()) {
//					file.createNewFile();
//					file.delete();
//				} else if (Objects.nonNull(this.document)){
//					file = new File(ResourceBundleUtil.getString("local.path.document") + this.document.getFileName());
//					file = new File(ResourceBundleUtil.getString("server.path.document") + this.document.getFileName());
//					if (file.exists()) {
//						file.createNewFile();
//						file.delete();
//					}
//				}
			}

			InputStream inputStream = event.getFile().getInputstream();
			OutputStream outputStream = new FileOutputStream(file);
			IOUtils.copy(inputStream, outputStream);
			inputStream.close();

			file.createNewFile();

			outputStream.flush();
			outputStream.close();

			document.setFilePath(file.getPath());
			document.setFileName(file.getName());
			
			if (!this.isEdit) {
				document.setNumberDL(0L);
			}
			this.showMessageWARN("", "", "Bạn đã upload file thành công vui lòng chọn Lưu lại để hoàn thành tiến trình");
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
	public String generateFileName(String fileName){
		LocalDateTime localDateTime = LocalDateTime.now();
		String ext = fileName.substring(fileName.indexOf(".pdf"), fileName.length());
		fileName = fileName.replace(".pdf", "");
		fileName += "_" + String.valueOf(localDateTime.getYear()) + String.valueOf(localDateTime.getMonthValue()) + String.valueOf(localDateTime.getDayOfMonth()) +
				String.valueOf(localDateTime.getHour()) + String.valueOf(localDateTime.getMinute()) + String.valueOf(localDateTime.getSecond()) + ext;
		return fileName;
	}

	public void showDialogDoc(Document document) {
		if (document == null) {
			this.document = new Document();
			this.document.setUploadDate(new Date());
			this.isEdit = false;
			this.isUpload = true;
		} else {
			this.document = document;
			this.isEdit = true;
			this.isUpload = false;
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

	public List<Document> getFilteredDocuments() {
		return filteredDocuments;
	}

	public void setFilteredDocuments(List<Document> filteredDocuments) {
		this.filteredDocuments = filteredDocuments;
	}

	public Boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}

	public FileUploadEvent getEventUpload() {
		return eventUpload;
	}

	public void setEventUpload(FileUploadEvent eventUpload) {
		this.eventUpload = eventUpload;
	}

	public Boolean getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(Boolean isUpload) {
		this.isUpload = isUpload;
	}
	
}
