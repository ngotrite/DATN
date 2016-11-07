package vn.edu.nuce.datn.db;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;

public class HibernateUtil {

	private static SessionFactory sessionFactory = null;

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration cf = new Configuration().configure("resources/hibernate.cfg.xml");
			return cf.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}
		return sessionFactory;
	}

	public static Session getCurrentSession() {
		if (sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}
		return sessionFactory.getCurrentSession();
	}

	public static Session getOpenSession() {
		if (sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}
		Session session = sessionFactory.openSession();
		return session;
	}

	public static void copyEntityProperties(Object objFrom, Object objTo) {

		copyEntityProperties(null, objFrom, objTo, true);
	}

	public static void copyEntityProperties(Class classBO, Object objFrom, Object objTo, boolean isCopyID) {

		if (objFrom == null || objTo == null)
			return;
		BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);

		try {

			if (!isCopyID) {

				ClassMetadata classMetadata = HibernateUtil.getSessionFactory().getClassMetadata(classBO);
				String identifierPropertyName = classMetadata.getIdentifierPropertyName();

				if (identifierPropertyName != null) {

					Object oldValue = PropertyUtils.getProperty(objTo, identifierPropertyName);
					BeanUtils.copyProperties(objTo, objFrom);
					PropertyUtils.setProperty(objTo, identifierPropertyName, oldValue);
				} else {

					BeanUtils.copyProperties(objTo, objFrom);
				}
			} else {
				BeanUtils.copyProperties(objTo, objFrom);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void shutdown() {
		// Close caches and connection pools
		getSessionFactory().close();
	}
}
