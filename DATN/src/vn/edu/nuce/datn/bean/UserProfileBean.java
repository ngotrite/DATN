package vn.edu.nuce.datn.bean;


import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.language.bm.Lang;

import vn.edu.nuce.datn.dao.SysRoleDAO;
import vn.edu.nuce.datn.dao.SysUserDAO;
import vn.edu.nuce.datn.entity.SysUser;
import vn.edu.nuce.datn.util.PasswordUtil;
import vn.edu.nuce.datn.util.SessionUtils;



@ManagedBean(name = "userProfileBean")
@SessionScoped
public class UserProfileBean extends BaseController implements Serializable {

	private static final long serialVersionUID = 9089358585644225640L;
	private SysUser user;
	private SysUser sysUserPass;
	private String password;
	private String newPassword;
	private String ReenterPassword;
	private String role;
	private SysRoleDAO sysRoleDAO;
private SysUserDAO sysUserDAO;
	private List<Lang> listLang;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getReenterPassword() {
		return ReenterPassword;
	}

	public void setReenterPassword(String reenterPassword) {
		ReenterPassword = reenterPassword;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}

	public List<Lang> getListLang() {
		return listLang;
	}

	public void setListLang(List<Lang> listLang) {
		this.listLang = listLang;
	}

	@PostConstruct
	private void init() {
		user = new SysUser();
		sysUserPass = new SysUser();
	}

	public void getUserByLogin() {
		user = SessionUtils.getUser();
	}
	
	public void btnSave() {
		SysUserDAO sysUserDAO = new SysUserDAO();
		sysUserDAO.saveOrUpdate(user);
		this.showMessageINFO("common.save", " User profile");
	}

	public void doChangePassword() {
		if (validateSavePass()) {
			
			SysUserDAO userDao = new SysUserDAO();
			
			sysUserPass = userDao.findByUserName(SessionUtils.getUserName(), 0);
			sysUserPass.setPassword(PasswordUtil.generateHash(newPassword));
			userDao.saveOrUpdate(sysUserPass);
			
			this.showMessageINFO("common.save", " Change Password ");
			HttpSession session = SessionUtils.getSession();
			session.invalidate();
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest req = (HttpServletRequest) ec.getRequest();
			try {
//				ec.redirect(req.getContextPath() + "/login.xhtml");
				ec.redirect(req.getContextPath() + "/admin/login.xhtml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean validateSavePass() {
		
		if(password == null || newPassword == null || ReenterPassword == null) {
			
			showNotification(FacesMessage.SEVERITY_WARN, "common.fail", "changepass.enterAllPass");
			return false;
		}
		
		if(!newPassword.equals(ReenterPassword)) {
			
			showNotification(FacesMessage.SEVERITY_WARN, "common.fail", "changepass.notMatch");
			return false;
		}
		

		if (!PasswordUtil.validatePassword(newPassword)) {
			super.showMessageWARN("common.save", " Password ", "user.formatPassword");
			return false;
		}
		
		SysUserDAO sysUserDAO = new SysUserDAO();
		SysUser sysUser = sysUserDAO.findByUserName(SessionUtils.getUserName(), 0);
		if (sysUser == null || !sysUser.getPassword().equalsIgnoreCase(PasswordUtil.generateHash(password))) {
			showNotification(FacesMessage.SEVERITY_WARN, "common.fail", "changepass.passwordincorrect");
			return false;
		}

		if (newPassword.equals(password)) {
			super.showMessageWARN("common.save", " lỗi ", "user.passUsed");
			return false;
		}
		
		return true;
	}

}
