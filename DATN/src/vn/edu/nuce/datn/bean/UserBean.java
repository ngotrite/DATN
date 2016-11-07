package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;

import vn.edu.nuce.datn.dao.SysRoleDAO;
import vn.edu.nuce.datn.dao.SysUserDAO;
import vn.edu.nuce.datn.entity.SysRole;
import vn.edu.nuce.datn.entity.SysUser;

@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean extends BaseController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7569728160086294584L;
	private SysUser sysUser = new SysUser();
	private List<SysUser> listSysUser;
	private List<SysRole> listSysRole;
	private long roleID;
	private boolean isEditing;

	private SysUserDAO userDao;

	public UserBean() {

		userDao = new SysUserDAO();
		init();
		searchUser();
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	private void init() {

		sysUser = new SysUser();
		searchRole();
	}

	private void searchUser() {
		SysUserDAO baseDAO = new SysUserDAO();
		listSysUser = baseDAO.findAll();
	}

	private void searchRole() {

		SysRoleDAO roleDao = new SysRoleDAO();
		listSysRole = roleDao.findAll();
	}

	public void btnNew() {

		init();
		isEditing = true;
	}

	public void btnCancel() {

		init();
		isEditing = false;
	}

	public void btnSave() {

		sysUser.setRole(new SysRoleDAO().get(getRoleID()));
		if (sysUser.getId() > 0) {

			userDao.update(sysUser);
		} else {

			userDao.save(sysUser);
			listSysUser.add(sysUser);
		}

		btnCancel();
	}

	public void onRowSelect(SelectEvent event) {
		// sysUser = (SysUser) event.getObject();
		// roleID = sysUser.getRole().getId();
	}

	public void onRowEdit(SysUser user) {

		this.sysUser = user;
		roleID = sysUser.getRole().getId();
		isEditing = true;
	}

	public void onRowDelete(SysUser user) {

		if (this.sysUser.getId() == user.getId()) {
			btnCancel();
		}
		userDao.delete(user);
		listSysUser.remove(user);
	}

	/** GET SET **/
	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public List<SysUser> getListSysUser() {
		return listSysUser;
	}

	public void setListSysUser(List<SysUser> listSysUser) {
		this.listSysUser = listSysUser;
	}

	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	public List<SysRole> getListSysRole() {
		return listSysRole;
	}

	public void setListSysRole(List<SysRole> listSysRole) {
		this.listSysRole = listSysRole;
	}

	public boolean isEditing() {
		return isEditing;
	}

	public void setEditing(boolean isEditing) {
		this.isEditing = isEditing;
	}
}
