package vn.edu.nuce.datn.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
//import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.query.Query;

import vn.edu.nuce.datn.db.HibernateUtil;
import vn.edu.nuce.datn.db.Operator;
import vn.edu.nuce.datn.util.CommonUtil;

/**
 * Use manual Hibernate implement
 * 
 * @author huannn
 *
 * @param <T>
 */
public abstract class BaseDAO<T> {


	public BaseDAO() {

	}


	public List<T> findAll() {

		Session session = HibernateUtil.getOpenSession();
		List<T> lst = null;
		try {
			Criteria cri = session.createCriteria(getEntityClass());
			lst = cri.list();
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}

		return lst;
	}
	
	public int countByConditions(String[] cols, Operator[] operators, Object[] values) {
		return this.countByConditions(getEntityClass(), cols, operators, values);
	}
	public <E> int countByConditions(Class<E> clazz, String[] cols, Operator[] operators, Object[] values) {

		Session session = HibernateUtil.getOpenSession();
		List lst = null;
		try {

			// Criteria cri = session.createCriteria(clazz);
			String queryString = "SELECT COUNT(obj) FROM " + clazz.getName();
			queryString += getWhereString(cols, operators);

			Query query = session.createQuery(queryString);
			setWhereParam(cols, operators, values, query);
			lst = query.getResultList();
			if(lst.size() > 0)
				return ((Number)lst.get(0)).intValue();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}

		return 0;
	}

	public List<T> findByConditions(String[] cols, Operator[] operators, Object[] values, String orders) {
		return this.findByConditions(getEntityClass(), cols, operators, values, orders);
	}
	public <E> List<E> findByConditions(Class<E> clazz, String[] cols, Operator[] operators, Object[] values, String orders) {

		Session session = HibernateUtil.getOpenSession();
		List<E> lst = null;
		try {

			// Criteria cri = session.createCriteria(clazz);
			String queryString = "SELECT obj FROM " + clazz.getName();
			queryString += getWhereString(cols, operators);

			if (!CommonUtil.isEmpty(orders))
				queryString += " ORDER BY " + orders;

			Query<E> query = session.createQuery(queryString);
			setWhereParam(cols, operators, values, query);
			lst = query.getResultList();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}

		return lst;
	}
	
	public List<T> findByConditionsWithoutDomain(String[] cols, Operator[] operators, Object[] values, String orders) {
		return this.findByConditionsWithoutDomain(getEntityClass(), cols, operators, values, orders);
	}
	public <E> List<E> findByConditionsWithoutDomain(Class<E> clazz, String[] cols, Operator[] operators, Object[] values, String orders) {

		Session session = HibernateUtil.getOpenSession();
		List<E> lst = null;
		try {

			// Criteria cri = session.createCriteria(clazz);
			String queryString = "SELECT obj FROM " + clazz.getName() + " obj WHERE 1 = 1 ";
			queryString += getWhereString(cols, operators);

			if (!CommonUtil.isEmpty(orders))
				queryString += " ORDER BY " + orders;

			Query<E> query = session.createQuery(queryString);
			setWhereParam(cols, operators, values, query);
			lst = query.getResultList();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}

		return lst;
	}


	private <E> void setWhereParam(String[] cols, Operator[] operators, Object[] values, Query<E> query) {
		for (int i = 0; i < cols.length; i++) {
			if (Operator.IN.equals(operators[i])) {

				if (values[i] instanceof Object[]) {

					query.setParameterList(cols[i], (Object[]) values[i]);
				} else if (values[i] instanceof Collection) {

					query.setParameterList(cols[i], (Collection) values[i]);
				}
			} else if(Operator.NULL.equals(operators[i]) || Operator.NOTNULL.equals(operators[i])) {
				//do nothing
			} else {
				query.setParameter(cols[i], values[i]);
			}
		}
	}

	private String getWhereString(String[] cols, Operator[] operators) {
		String colName;
		Operator operator;
		String whereString = "";
		for (int i = 0; i < cols.length; i++) {

			colName = cols[i];
			operator = operators[i];
			if (operator == Operator.EQ) {
				whereString += " AND " + colName + " = :" + colName;
			} else if (operator == Operator.NOTEQ) {
				whereString += " AND " + colName + " <> :" + colName;
			} else if (operator == Operator.GT) {
				whereString += " AND " + colName + " > :" + colName;
			} else if (operator == Operator.GE) {
				whereString += " AND " + colName + " >= :" + colName;
			} else if (operator == Operator.LT) {
				whereString += " AND " + colName + " < :" + colName;
			} else if (operator == Operator.LE) {
				whereString += " AND " + colName + " <= :" + colName;
			} else if (operator == Operator.LIKE) {
				whereString += " AND " + colName + " LIKE :" + colName;
			} else if (operator == Operator.ILIKE) {
				whereString += " AND " + colName + " LIKE '%:" + colName + "%'";
			} else if (operator == Operator.IN) {
				whereString += " AND " + colName + " IN (:" + colName + ")";
			} else if (operator == Operator.NOTIN) {
				whereString += " AND " + colName + " NOT IN (:" + colName + ")";
			} else if (operator == Operator.NULL) {
				whereString += " AND " + colName + " IS NULL";
			} else if (operator == Operator.NOTNULL) {
				whereString += " AND " + colName + " IS NOT NULL";
			}
		}
		return whereString;
	}

	public T get(Integer id) {
		
		if(id == null)
			return null;

		Session session = HibernateUtil.getOpenSession();
		T t = null;
		try {
			t = (T) session.get(getEntityClass(), id);
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return t;
	}
	
	public T get(String id) {
		
		if(id == null)
			return null;

		Session session = HibernateUtil.getOpenSession();
		T t = null;
		try {
			t = (T) session.get(getEntityClass(), id);
		} catch (Exception e) {
//			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return t;
	}
	
	public T get(Long id) {
		
		if(id == null)
			return null;

		Session session = HibernateUtil.getOpenSession();
		T t = null;
		try {
			t = (T) session.get(getEntityClass(), id);
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return t;
	}

	public T load(Integer id) {

		Session session = HibernateUtil.getOpenSession();
		T t = null;
		try {
			t = (T) session.load(getEntityClass(), id);
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return t;
	}
	
	public T load(Long id) {

		Session session = HibernateUtil.getOpenSession();
		T t = null;
		try {
			t = (T) session.load(getEntityClass(), id);
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
		return t;
	}


	public void save(T t) {

		if (t == null)
			return;

		Session session = HibernateUtil.getOpenSession();
		try {

			session.beginTransaction();
			session.save(t);
			session.getTransaction().commit();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}

	}

	public void saveOrUpdate(T t) {

		if (t == null)
			return;
		Session session = HibernateUtil.getOpenSession();
		try {

			session.beginTransaction();
			session.saveOrUpdate(t);
			session.getTransaction().commit();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public void update(T t) {

		if (t == null)
			return;
		Session session = HibernateUtil.getOpenSession();
		try {
			session.beginTransaction();
			session.update(t);
			session.getTransaction().commit();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public void deleteByConditions(String[] cols, Operator[] operators, Object[] values) {
		this.deleteByConditions(getEntityClass(), cols, operators, values);
	}
	
	/**
	 * 
	 * @param clazz
	 * @param whereColName
	 * @param whereColValue
	 */
	public <E> void deleteByConditions(Class<E> clazz, String[] cols, Operator[] operators, Object[] values) {

		Session session = HibernateUtil.getOpenSession();
		try {
			session.beginTransaction();
			String hql = "DELETE " + clazz.getName() + " WHERE 1=1 ";
			hql += getWhereString(cols, operators);
			Query q = session.createQuery(hql);
			setWhereParam(cols, operators, values, q);
			q.executeUpdate();
			session.getTransaction().commit();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public void delete(T t) {

		if (t == null)
			return;
		Session session = HibernateUtil.getOpenSession();
		try {
			session.beginTransaction();
			session.delete(t);
			session.getTransaction().commit();
		} catch (Exception e) {
			//e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	abstract protected Class<T> getEntityClass();
}
