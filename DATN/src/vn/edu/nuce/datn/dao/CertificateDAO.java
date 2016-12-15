package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

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
	
	public List<Certificate> findCertificateHome(String studentId, String studentName, Date birthday, String certificateNo) {
		List<Certificate> lst = null;
		String[] cols = { "studentId" , "studentName", "birthday" , "certificateNo" };
		Operator[] operators = { Operator.LIKE , Operator.LIKE , Operator.LIKE, Operator.LIKE };
		Object[] values = { studentId , studentName, birthday, certificateNo};
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
//	public List<Certificate> findCertificateHome(String studentId) {
//		List<Certificate> lst = null;
//		String[] cols = { "studentId" };
//		Operator[] operators = { Operator.EQ };
//		Object[] values = {studentId};
//		lst = findByConditionsWithoutDomain(cols, operators, values, "");
//		return lst;
//	}
//	
	
	
}
