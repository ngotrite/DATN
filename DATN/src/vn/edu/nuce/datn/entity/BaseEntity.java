package vn.edu.nuce.datn.entity;
import java.io.Serializable;

import javax.persistence.Transient;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.metadata.ClassMetadata;

import vn.edu.nuce.datn.db.ExcludeFieldJson;
import vn.edu.nuce.datn.db.HibernateUtil;



//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5384391712628869118L;
	@Transient
	protected String nodeName;
	public abstract String getNodeName();
	
	public long getID() {
		try {
			
			ClassMetadata classMetadata = HibernateUtil.getSessionFactory().getClassMetadata(getClass());
			String identifierPropertyName = classMetadata.getIdentifierPropertyName();
			return (long) PropertyUtils.getProperty(this, identifierPropertyName);	
		} catch (Exception e) {
			
			//e.printStackTrace();
			return -1L;
		}
	}

	public String getUniqueKey() {
		
		try {
			
			ClassMetadata classMetadata = HibernateUtil.getSessionFactory().getClassMetadata(getClass());
			String identifierPropertyName = classMetadata.getIdentifierPropertyName();
			return getClass().getName() + "-" +  PropertyUtils.getProperty(this, identifierPropertyName);	
		} catch (Exception e) {
			
			//e.printStackTrace();
			return "ENTITY NO KEY VALUE";
		}		
	}
	
	@Transient
	@ExcludeFieldJson
	private int treePosIdx = -1;
	public int getTreePosIdx() {
		
		if(treePosIdx < 0) {
			
		}
		return treePosIdx;
	}
	
	public void setTreePosIdx(int treePosIdx) {
		
		this.treePosIdx = treePosIdx;
	}
}
