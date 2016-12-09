package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import vn.edu.nuce.datn.dao.NewsDAO;
import vn.edu.nuce.datn.entity.News;

@SuppressWarnings("serial")
@ManagedBean(name = "newsDetailBean")
@ViewScoped
public class NewsDetailBean extends BaseController implements Serializable{
	
	private News news;
	private NewsDAO newsDAO;
	
	
	@PostConstruct
	public void init() {
		this.news = new News();
		this.newsDAO = new NewsDAO();
	}


	public News getNews() {
		return news;
	}


	public void setNews(News news) {
		this.news = news;
	}

}
