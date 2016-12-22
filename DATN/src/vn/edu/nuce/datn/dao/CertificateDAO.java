package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.entity.Certificate;

@SuppressWarnings("serial")
public class CertificateDAO extends BaseDAO<Certificate> implements Serializable {
	@Override
	protected Class<Certificate> getEntityClass() {
		return Certificate.class;
	}

	public void saveCertificate(List<Certificate> lst) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {

			for (Certificate cer : lst) {
				session.saveOrUpdate(cer);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

	}

	public List<Certificate> findCertificateHomeNew(String studentId, String studentName, Date birthday,
			String certificateNo) {
		List<Certificate> lstCertificate = new ArrayList<Certificate>();
		Session session = HibernateUtil.getOpenSession();
		StringBuilder sql = new StringBuilder();

		sql.append("FROM Certificate c");

		sql.append(" WHERE 1=1");

		if (!studentId.isEmpty()) {
			sql.append(" AND c.studentId=:studentId ");
		}

		if (!studentName.isEmpty()) {
			sql.append(" AND c.studentName=:studentName ");
		}

		if (birthday != null) {
			sql.append(" AND c.birthday=:birthday ");
		}

		if (!certificateNo.isEmpty()) {
			sql.append(" AND c.certificateNo=:certificateNo ");
		}

		Query query = session.createQuery(sql.toString());

		if (!studentId.isEmpty()) {
			query.setParameter("studentId", studentId);
		}

		if (!studentName.isEmpty()) {
			query.setParameter("studentName", studentName);
		}

		if (birthday != null) {
			query.setParameter("birthday", birthday);
		}

		if (!certificateNo.isEmpty()) {
			query.setParameter("certificateNo", certificateNo);
		}

		lstCertificate = query.getResultList();

		return lstCertificate;

	}

	public List<Certificate> findCertificateHome(String studentId, String studentName, Date birthday,
			String certificateNo) {
		List<Certificate> lst = null;
		String[] cols = { "studentId", "studentName", "birthday", "certificateNo" };
		Operator[] operators = { Operator.EQ, Operator.EQ, Operator.EQ, Operator.EQ };
		Object[] values = { studentId, studentName, birthday, certificateNo };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}

//	Select count(*) from certificate c Where c.GRADUATION_YEAR = '2016' and c.MAJOR=6;

	public Long countCer(String graduationYear, Long major) {
		Session session = HibernateUtil.getOpenSession();
		String sql = "Select count(*) FROM certificate c WHERE c.GRADUATION_YEAR =:graduationYear AND c.MAJOR =:major";
		Query query = session.createNativeQuery(sql);
		query.setParameter("graduationYear", graduationYear);
		query.setParameter("major", major);
		
		Number number = (Number) query.getSingleResult();

		return number.longValue();
	}

}
