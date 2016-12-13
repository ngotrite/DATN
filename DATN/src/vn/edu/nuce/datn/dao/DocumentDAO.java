package vn.edu.nuce.datn.dao;

import java.io.Serializable;

import vn.edu.nuce.datn.entity.Document;

public class DocumentDAO extends BaseDAO<Document> implements Serializable{
	@Override
	protected Class<Document> getEntityClass() {
		return Document.class;
	}

}
