package vn.edu.nuce.datn.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import vn.edu.nuce.datn.dao.NewsDAO;
import vn.edu.nuce.datn.entity.News;

@SuppressWarnings("serial")
@ManagedBean(name = "newsBean")
@ViewScoped
public class NewsBean extends BaseController implements Serializable {

	private News news;
	private List<News> lstNews;
	private NewsDAO newsDAO;
	private String formType;
	
	@PostConstruct
	public void init() {
		this.news = new News();
		this.newsDAO = new NewsDAO();
		this.lstNews = new ArrayList<News>();
		loadLstNews();
		formType = "";
	}

	public void loadLstNews() {
		lstNews = newsDAO.findAll("postDate");
	}

	public void cmdAddNew() {
		news = new News();
	}

	public void cmdApply() {
		try {
			newsDAO.saveOrUpdate(news);
			loadLstNews();
			super.showNotificationSuccsess();
		} catch (Exception e) {
			e.printStackTrace();
			super.showNotificationFail();
		}
	}

	public void cmdCancel() {
		news = new News();
	}

	public void cmdDelele(News item) {
		try {
			newsDAO.delete(item);
			lstNews.remove(item);
			news  = new News();
			super.showNotificationSuccsess();
		} catch (Exception e) {
			e.printStackTrace();
			super.showNotificationFail();
		}
	}

	public void cmdEdit(News item) {
		this.news = item;
	}
	
	
//	@ManagedProperty("#{newsDetailBean}")
//	private NewsDetailBean newsDetailBean;
//	
//	public void setNewsDetailBean(NewsDetailBean newsDetailBean) {
//		this.newsDetailBean = newsDetailBean;
//	}
	public void cmdViewDetail(News item) {
		this.news = item;
		setFormType("home-detail");
//		formType = "home-detail";
//		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
//		ec.redirect(req.getContextPath() + "/home-detail.xhtml");
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public List<News> getLstNews() {
		return lstNews;
	}

	public void setLstNews(List<News> lstNews) {
		this.lstNews = lstNews;
	}

	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}
	
}
