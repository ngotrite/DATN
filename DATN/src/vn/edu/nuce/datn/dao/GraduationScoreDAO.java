package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.entity.GraduationScore;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.entity.TestScore;

@SuppressWarnings("serial")
public class GraduationScoreDAO extends BaseDAO<GraduationScore> implements Serializable {
	@Override
	protected Class<GraduationScore> getEntityClass() {
		return GraduationScore.class;
	}

	public boolean delListGS(List<GraduationScore> graScores) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		List<String> graScoreIds = new ArrayList<String>();
		if (graScores.size() > 0) {
			for (GraduationScore gs : graScores) {
				graScoreIds.add(gs.getStudentId());
			}
		}
		try {
			StringBuffer hql = new StringBuffer("DELETE FROM GraduationScore b");
			hql.append(" WHERE b.studentId IN (:graScoreIds)");

			Query<GraduationScore> query = session.createQuery(hql.toString());
			query.setParameterList("graScoreIds", graScoreIds);
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

	public void saveGS(List<GraduationScore> graScores) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {

			for (GraduationScore graduationScore : graScores) {
				session.saveOrUpdate(graduationScore);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}
	
	public boolean checkStudentIdInGraduationScore(String studentId) {
		List<GraduationScore> lst = null;
		String[] cols = { "studentId" };
		Operator[] operators = { Operator.EQ };
		Object[] values = { studentId };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		if (lst != null && lst.size() > 0) {
			return true;
		}
		return false;
	}
	
	public Boolean checkFieldIsExist(String col, String value, GraduationScore graduationScore) {
		boolean result = false;
		int count = 0;
		if (graduationScore == null) {
			String[] column = { col };
			Operator[] ope = { Operator.EQ };
			Object[] val = { value };
			count = this.countByConditions(column, ope, val);
		} else {
			String[] column = { col, "studentId" };
			Operator[] ope = { Operator.EQ, Operator.NOTEQ };
			Object[] val = { value, graduationScore.getStudentId()};
			count = this.countByConditions(column, ope, val);
		}
		if (count > 0) {
			result = true;
		}
		return result;
	}
	
	public List<GraduationScore> getLstCheckGraduationScore(String createDate) {
		Session session = HibernateUtil.getOpenSession();
		List<GraduationScore> lst = null;
		try {

			String queryString = "SELECT * FROM  graduation_score WHERE DATE_FORMAT(CREATE_DATE, '%Y%m%d') >= " + createDate;
			Query query = session.createNativeQuery(queryString, GraduationScore.class); 
			lst = query.getResultList();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return lst;
	}

}
