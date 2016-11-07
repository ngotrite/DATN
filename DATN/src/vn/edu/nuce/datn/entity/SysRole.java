package vn.edu.nuce.datn.entity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="sys_role")
public class SysRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5748320798053404000L;
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="name", nullable=false, unique=true)
	private String name;
	
	@Column(name="role_type")
	private String roleType;
	
	@Column(name="description")
	private String description;
	
	@OneToMany(mappedBy="role")
	private Set<SysUser> users = new HashSet<SysUser>();
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Set<SysUser> getUsers() {
		return users;
	}

	public void setUsers(Set<SysUser> users) {
		this.users = users;
	}
	
}
