package vn.edu.nuce.datn.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * SortPriceRateTableMap generated by hbm2java
 */
@Entity
@Table(name="sys_user_role_map"
)
public class SysUserRoleMap extends BaseEntity  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4357105757422223275L;

	@Override
	@Transient
	public String getNodeName() {
		return null;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
     private long id;
     @Column(name="user_id")     
     private long userId;
     @Column(name="role_id")
     private long roleId;

    public SysUserRoleMap() {
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
}
