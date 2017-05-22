package vn.edu.nuce.datn.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;


import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.entity.GraduationPeriod;
import vn.edu.nuce.datn.entity.Student;
import vn.edu.nuce.datn.entity.SubjectDictionary;

@SuppressWarnings("serial")
public class StudentDAO extends BaseDAO<Student> implements Serializable {
	
	public List<Student> findSTByGP(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" };
		Operator[] operators = { Operator.EQ };
		Object[] values = { graduationPeriodId };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
	public List<Student> findSTByGPOCOM(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" , "oComStatus"};
		Operator[] operators = { Operator.EQ , Operator.EQ};
		Object[] values = { graduationPeriodId , true };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
	public List<Student> findSTFinishedByGP(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" , "schoolFeeStatus", "libraryStatus" , "departmentStatus" };
		Operator[] operators = { Operator.EQ , Operator.EQ , Operator.EQ, Operator.EQ };
		Object[] values = { graduationPeriodId , true, true, true};
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
	public List<Student> findSTFinishedByGPmT(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" , "schoolFeeStatus", "libraryStatus" , "departmentStatus" , "oComStatus", "mComStatus" };
		Operator[] operators = { Operator.EQ , Operator.EQ , Operator.EQ, Operator.EQ, Operator.EQ, Operator.EQ };
		Object[] values = { graduationPeriodId , true, true, true, true, true};
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
	public List<Student> findSTFinishedByGPmF(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" , "schoolFeeStatus", "libraryStatus" , "departmentStatus" , "oComStatus", "mComStatus" };
		Operator[] operators = { Operator.EQ , Operator.EQ , Operator.EQ, Operator.EQ, Operator.EQ, Operator.EQ };
		Object[] values = { graduationPeriodId , true, true, true, false, false};
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
	public List<Student> findSTFinishedByGPmFT(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" , "schoolFeeStatus", "libraryStatus" , "departmentStatus" , "oComStatus", "mComStatus" };
		Operator[] operators = { Operator.EQ , Operator.EQ , Operator.EQ, Operator.EQ, Operator.EQ, Operator.EQ };
		Object[] values = { graduationPeriodId , true, true, true, false, true};
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}

	public List<Student> findSTUFinishedByGP(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" , "schoolFeeStatus", "libraryStatus" , "departmentStatus" };
		Operator[] operators = { Operator.EQ , Operator.EQ , Operator.EQ, Operator.EQ };
		Object[] values = { graduationPeriodId , false, false, false};
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}
	
	public List<Student> findSTSFFinishedByGP(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId" , "schoolFeeStatus"};
		Operator[] operators = { Operator.EQ , Operator.EQ };
		Object[] values = { graduationPeriodId , false};
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		return lst;
	}


	public void saveStudents(List<Student> students) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			for (Student student : students) {
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
	
	public int countStudentbyGP(Long graduationPeriodId) {
		String hql = "SELECT COUNT(a) FROM Student a WHERE  a.graduationPeriodId =:graduationPeriodId ";
		Session session = HibernateUtil.getOpenSession();
		try {
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("graduationPeriodId", graduationPeriodId);
			return query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public int countStudentsSF(Long graduationPeriodId, boolean schoolFeeStatus) {
		String hql = "SELECT COUNT(a) FROM Student a WHERE  a.graduationPeriodId =:graduationPeriodId ";
		hql += " AND a.schoolFeeStatus = :schoolFeeStatus";
		Session session = HibernateUtil.getOpenSession();
		try {
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("graduationPeriodId", graduationPeriodId);
			query.setParameter("schoolFeeStatus", schoolFeeStatus);
			return query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public int countStudentsD(Long graduationPeriodId, boolean departmentStatus) {
		String hql = "SELECT COUNT(a) FROM Student a WHERE  a.graduationPeriodId =:graduationPeriodId ";
		hql += " AND a.departmentStatus = :departmentStatus";
		Session session = HibernateUtil.getOpenSession();
		try {
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("graduationPeriodId", graduationPeriodId);
			query.setParameter("departmentStatus", departmentStatus);
			return query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public int countStudentsL(Long graduationPeriodId, boolean libraryStatus) {
		String hql = "SELECT COUNT(a) FROM Student a WHERE  a.graduationPeriodId =:graduationPeriodId ";
		hql += " AND a.libraryStatus = :libraryStatus";
		Session session = HibernateUtil.getOpenSession();
		try {
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("graduationPeriodId", graduationPeriodId);
			query.setParameter("libraryStatus", libraryStatus);
			return query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public int countStudentsM(Long graduationPeriodId, boolean mComStatus, boolean oComStatus ) {
		String hql = "SELECT COUNT(a) FROM Student a WHERE  a.graduationPeriodId =:graduationPeriodId ";
		hql += " AND a.mComStatus = :mComStatus";
		hql += " AND a.oComStatus = :oComStatus";
		Session session = HibernateUtil.getOpenSession();
		try {
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("graduationPeriodId", graduationPeriodId);
			query.setParameter("mComStatus", mComStatus);
			query.setParameter("oComStatus", oComStatus);
			return query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public int countStudentsO(Long graduationPeriodId, boolean oComStatus ) {
		String hql = "SELECT COUNT(a) FROM Student a WHERE  a.graduationPeriodId =:graduationPeriodId ";
		hql += " AND a.oComStatus = :oComStatus";
		Session session = HibernateUtil.getOpenSession();
		try {
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("graduationPeriodId", graduationPeriodId);
			query.setParameter("oComStatus", oComStatus);
			return query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public int countStudentsB(Long graduationPeriodId, boolean briefStatus) {
		String hql = "SELECT COUNT(a) FROM Student a WHERE  a.graduationPeriodId =:graduationPeriodId ";
		hql += " AND a.briefStatus = :briefStatus";
		Session session = HibernateUtil.getOpenSession();
		try {
			Query<Long> query = session.createQuery(hql, Long.class);
			query.setParameter("graduationPeriodId", graduationPeriodId);
			query.setParameter("briefStatus", briefStatus);
			return query.getSingleResult().intValue();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public List<Student> checkStudentIdNew(String studentId) {
		List<Student> lst = new ArrayList<>();
		String[] cols = { "studentId"};
		Operator[] operators = { Operator.EQ};
		Object[] values = { studentId  };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		
		return lst;
	}
	
	public boolean updateStudentStatus(List<String> studentId, Date date, Long userId){
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			StringBuffer hql = new StringBuffer("UPDATE student SET DEPARTMENT_STATUS = 1, DEPARTMENT_UPDATE_TIME = :updatedDate, DEPARTMENT_USER_ID = :userId ");
			hql.append(" WHERE STUDENT_ID IN (:studentsId)");

			Query<Student> query = session.createNativeQuery(hql.toString());
			query.setParameterList("studentsId", studentId);
			query.setParameter("updatedDate", date);
			query.setParameter("userId", userId);
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
	
	public boolean updateStudentStatusF(List<String> studentId, Date date, Long userId){
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			StringBuffer hql = new StringBuffer("UPDATE student SET SCHOOL_FEE_STATUS = 1, SCHOOL_FEE_UPDATE_TIME = :updatedDate, SCHOOL_FEE_USER_ID = :userId ");
			hql.append(" WHERE STUDENT_ID IN (:studentsId)");

			Query<Student> query = session.createNativeQuery(hql.toString());
			query.setParameterList("studentsId", studentId);
			query.setParameter("updatedDate", date);
			query.setParameter("userId", userId);
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
	
	public boolean updateStudentStatusL(List<String> studentId, Date date, Long userId){
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			StringBuffer hql = new StringBuffer("UPDATE student SET LIBRARY_STATUS = 1, LIBRARY_UPDATE_TIME = :updatedDate, LIBRARY_USER_ID = :userId ");
			hql.append(" WHERE STUDENT_ID IN (:studentsId)");

			Query<Student> query = session.createNativeQuery(hql.toString());
			query.setParameterList("studentsId", studentId);
			query.setParameter("updatedDate", date);
			query.setParameter("userId", userId);
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
	
	public boolean updateStudentStatusO(List<String> studentId, Date date, Long userId){
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			StringBuffer hql = new StringBuffer("UPDATE student SET MCOM_STATUS = 1, MCOM_UPDATE_TIME = :updatedDate, MCOM_USER_ID = :userId ");
			hql.append(" WHERE STUDENT_ID IN (:studentsId)");

			Query<Student> query = session.createNativeQuery(hql.toString());
			query.setParameterList("studentsId", studentId);
			query.setParameter("updatedDate", date);
			query.setParameter("userId", userId);
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
	
	public boolean updateStudentStatusB(List<String> studentId, Date date, Long userId){
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			StringBuffer hql = new StringBuffer("UPDATE student SET BRIEF_STATUS = 1, BRIEF_UPDATE_TIME = :updatedDate, BRIEF_USER_ID = :userId ");
			hql.append(" WHERE STUDENT_ID IN (:studentsId)");

			Query<Student> query = session.createNativeQuery(hql.toString());
			query.setParameterList("studentsId", studentId);
			query.setParameter("updatedDate", date);
			query.setParameter("userId", userId);
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
	
	public boolean checkStudentId(String studentId) {
		List<Student> lst = null;
		String[] cols = { "studentId"};
		Operator[] operators = { Operator.EQ};
		Object[] values = { studentId  };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		if (lst != null && lst.size() > 0) {
			return true;
		}
		return false;
	}
	
	public boolean checkGPInStudent(Long graduationPeriodId) {
		List<Student> lst = null;
		String[] cols = { "graduationPeriodId"};
		Operator[] operators = { Operator.EQ};
		Object[] values = { graduationPeriodId };
		lst = findByConditionsWithoutDomain(cols, operators, values, "");
		if (lst != null && lst.size() > 0) {
			return true;
		}
		return false;
	}
	
	
	public Boolean checkFieldIsExist(String col, String value, Student student) {
		boolean result = false;
		int count = 0;
		if (student == null) {
			String[] column = { col };
			Operator[] ope = { Operator.EQ };
			Object[] val = { value };
			count = this.countByConditions(column, ope, val);
		} else {
			String[] column = { col, "studentId" };
			Operator[] ope = { Operator.EQ, Operator.NOTEQ };
			Object[] val = { value, student.getStudentId()};
			count = this.countByConditions(column, ope, val);
		}
		if (count > 0) {
			result = true;
		}
		return result;
	}
	
	public boolean delListStudent(List<Student> students) {
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		List<String> studentsId = new ArrayList<String>();
		if (students.size() > 0) {
			for (Student st : students) {
				studentsId.add(st.getStudentId());
			}
		}
		try {
			StringBuffer hql = new StringBuffer("DELETE FROM Student b");
			hql.append(" WHERE b.studentId IN (:studentsId)");

			Query<Student> query = session.createQuery(hql.toString());
			query.setParameterList("studentsId", studentsId);
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
	
	public Boolean updateStudents(List<Student> lstST) {
		boolean result = true;
		Session session = HibernateUtil.getOpenSession();
		session.getTransaction().begin();
		try {
			// UPDATE STUDENTS
			for (int i = 0; i < lstST.size(); i++) {
				Student stMap = lstST.get(i);
				session.update(stMap);
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


	
	@Override
	protected Class<Student> getEntityClass() {
		return Student.class;
	}
}
