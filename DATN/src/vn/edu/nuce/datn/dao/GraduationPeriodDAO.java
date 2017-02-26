package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;

@SuppressWarnings("serial")
public class GraduationPeriodDAO extends BaseDAO<GraduationPeriod> implements Serializable {

	@Override
	protected Class<GraduationPeriod> getEntityClass() {
		return GraduationPeriod.class;
	}

	public void saveGraPeriod(GraduationPeriod graduationPeriod, List<Student> students) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			session.saveOrUpdate(graduationPeriod);
			for (Student student : students) {
				student.setGraduationPeriodId(graduationPeriod.getGraduationPeriodId());
				session.saveOrUpdate(student);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
	}

	public Boolean saveGraPeriodAndST(GraduationPeriod graduationPeriod, List<Student> lstST) {
		boolean result = true;
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {

			// SAVE GRADUATION PERIOD
			session.saveOrUpdate(graduationPeriod);
			// SAVE OR UPDATE STUDENTS
			
			for (int i = 0; i < lstST.size(); i++) {
				Student stMap = lstST.get(i);
				stMap.setGraduationPeriodId(graduationPeriod.getGraduationPeriodId());
				session.save(stMap);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			result = false;
			throw e;
		} finally {
			session.close();
		}
		return result;
	}
	
}
