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
import vn.edu.nuce.datn.util.CommonUtil;
import vn.edu.nuce.datn.util.PasswordUtil;



@ManagedBean(name = "userBean")
@ViewScoped
public class UserBean extends BaseController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7569728160086294584L;
	private SysUser sysUser = new SysUser();
	private String password;
	private List<SysUser> listSysUser;
	private List<SysRole> listSysRole;
//	private long roleID;
	private boolean isEditing;
	
	private String pass;

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
		sysUser.setIsActive(true);
		searchRole();
	}

	private void searchUser() {
		SysUserDAO baseDAO = new SysUserDAO();
		listSysUser = baseDAO.findAll("");
	}

	private void searchRole() {

		SysRoleDAO roleDao = new SysRoleDAO();
		listSysRole = roleDao.findAll("");
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

		if(!this.validateSave())
			return;
				
		if (sysUser.getId() > 0) {

			userDao.update(sysUser);
		} else {
			sysUser.setLanguague("vi");
			userDao.save(sysUser);
			listSysUser.add(sysUser);
		}

		btnCancel();
		this.showMessageINFO("common.save", " User ");
	}

	private boolean validateSave() {
		
		SysUser checkObj = userDao.findByUserName(sysUser.getUserName(), sysUser.getId());
		if(checkObj != null) {
			
			super.showMessageERROR("common.save", " User ", "common.duplicateName");
			return false;
		}
		
		if(sysUser.getId() <= 0) {
			// Neu them moi thi bat buoc validate password
			
			if(!PasswordUtil.validatePassword(password)) {
				
				super.showMessageERROR("common.save", " User ", "user.formatPassword");
				return false;
			} else {
				sysUser.setPassword(PasswordUtil.generateHash(password));
			}
		} else {
			// Neu update thi chi validate password khi doi password
			
			if(!CommonUtil.isEmpty(password)) {				
				if(!PasswordUtil.validatePassword(password)) {
					
					super.showMessageERROR("common.save", " User ", "user.formatPassword");
					return false;
				} else {
					sysUser.setPassword(PasswordUtil.generateHash(password));
				}
			} else {
				// Do nothing
			}
		}
		if (!password.equals(pass)) {
//			super.showMessageINFO(actionKey, objName);
			return false;
		}
		
		return true;
	}
	

	public void onRowSelect(SelectEvent event) {
	}

	public void onRowEdit(SysUser user) {

		this.sysUser = user;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<SysUser> getListSysUser() {
		return listSysUser;
	}

	public void setListSysUser(List<SysUser> listSysUser) {
		this.listSysUser = listSysUser;
	}

//	public long getRoleID() {
//		return roleID;
//	}
//
//	public void setRoleID(long roleID) {
//		this.roleID = roleID;
//	}

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

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
