package vn.edu.nuce.datn.dao;

import java.io.Serializable;

import vn.edu.nuce.datn.entity.News;

@SuppressWarnings("serial")
public class NewsDAO extends BaseDAO<News> implements Serializable {
	
	@Override
	protected Class<News> getEntityClass() {
		return News.class;
	}
	
}
