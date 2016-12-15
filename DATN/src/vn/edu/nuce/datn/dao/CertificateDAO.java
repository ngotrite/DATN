package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.entity.Certificate;
import vn.edu.nuce.datn.entity.SubjectDictionary;

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
}
