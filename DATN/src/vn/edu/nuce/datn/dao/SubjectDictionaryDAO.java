package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;


import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.entity.SubjectDictionary;

public class SubjectDictionaryDAO extends BaseDAO<SubjectDictionary> implements Serializable{
	
	public Boolean checkFieldIsExist(String col, String value, SubjectDictionary subjectDictionary) {
		boolean result = false;

		int count = 0;

		if (subjectDictionary == null) {
			String[] column = { col };
			Operator[] ope = { Operator.EQ };
			Object[] val = { value };
			count = this.countByConditions(column, ope, val);
		} else {
			String[] column = { col, "subjectId" };
			Operator[] ope = { Operator.EQ, Operator.NOTEQ };
			Object[] val = { value, subjectDictionary.getSubjectId() };
			count = this.countByConditions(column, ope, val);
		}

		if (count > 0) {
			result = true;
		}

		return result;
	}
	
	public void saveSubjectDictionary(List<SubjectDictionary> subjectDictionaries) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {

			for (SubjectDictionary subjectDictionary : subjectDictionaries) {
				session.saveOrUpdate(subjectDictionary);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}
	
	public boolean delListSubjectDictionary(List<SubjectDictionary> subjectDictionaries) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		List<String> subjectDictionariesId = new ArrayList<String>();
		if (subjectDictionaries.size() > 0) {
			for (SubjectDictionary sd : subjectDictionaries) {
				subjectDictionariesId.add(sd.getSubjectId());
			}
		}
		try {
			StringBuffer hql = new StringBuffer("DELETE FROM SubjectDictionary b");
			hql.append(" WHERE b.subjectId IN (:subjectDictionariesId)");

			Query<SubjectDictionary> query = session.createQuery(hql.toString());
			query.setParameterList("subjectDictionariesId", subjectDictionariesId);
			query.executeUpdate();
			session.getTransaction().commit();
			return true;
		} catch (Exception e) {
			session.getTransaction().rollback();
			return false;
		} finally {
			session.close();
		}
	}
		

	@Override
	protected Class<SubjectDictionary> getEntityClass() {
		return SubjectDictionary.class;
	}



}
