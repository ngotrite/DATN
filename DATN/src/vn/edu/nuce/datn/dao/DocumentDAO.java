package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.entity.Document;

public class DocumentDAO extends BaseDAO<Document> implements Serializable {
	@Override
	protected Class<Document> getEntityClass() {
		return Document.class;
	}

	public List<Document> findAllDoc() {
		List<Document> lstFormulaErrorCode = new ArrayList<Document>();
		String[] cols = {};
		Operator[] operators = {};
		Object[] values = {};
		String order = "issuanceDate DESC";
		lstFormulaErrorCode = findByConditionsWithoutDomain(cols, operators, values, order);
		return lstFormulaErrorCode;
	}

}
