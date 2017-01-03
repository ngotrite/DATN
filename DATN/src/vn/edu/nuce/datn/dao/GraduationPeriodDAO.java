package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;


import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.entity.SubjectDictionary;

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

}
