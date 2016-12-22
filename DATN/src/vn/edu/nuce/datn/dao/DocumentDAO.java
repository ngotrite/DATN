package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import vn.edu.nuce.datn.db.HibernateUtil;
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

	public boolean checkName(Document document) {
		String hql = "SELECT COUNT(*) FROM Document a WHERE a.documentNumber LIKE :documentNumber AND a.documentId != :documentId";
		Session session = HibernateUtil.getOpenSession();
		try {

			Query<Long> query = session.createQuery(hql);
			query.setParameter("documentNumber", document.getDocumentNumber());
			query.setParameter("documentId", document.getDocumentId());
			int count = query.uniqueResult().intValue();
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

}
