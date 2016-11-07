package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.entity.SubjectDictionary;
import vn.edu.nuce.datn.entity.TestScore;

@SuppressWarnings("serial")
public class TestScoreDAO extends BaseDAO<TestScore> implements Serializable {

	@Override
	protected Class<TestScore> getEntityClass() {
		return TestScore.class;
	}

	public boolean delListTestScore(List<TestScore> testScores) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		List<Long> testScoreIds = new ArrayList<Long>();
		if (testScores.size() > 0) {
			for (TestScore sd : testScores) {
				testScoreIds.add(sd.getTestScoreId());
			}
		}
		try {
			StringBuffer hql = new StringBuffer("DELETE FROM TestScore b");
			hql.append(" WHERE b.testScoreId IN (:testScoreIds)");

			Query<TestScore> query = session.createQuery(hql.toString());
			query.setParameterList("testScoreIds", testScoreIds);
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

	public void saveTS(List<TestScore> testScores) {

		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {

			for (TestScore testScore : testScores) {
				session.saveOrUpdate(testScore);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}
}
