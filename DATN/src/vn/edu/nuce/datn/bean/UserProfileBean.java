package vn.edu.nuce.datn.bean;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vn.edu.nuce.datn.dao.SysRoleDAO;
import vn.edu.nuce.datn.dao.SysUserDAO;
import vn.edu.nuce.datn.entity.SysUser;
import vn.edu.nuce.datn.util.PasswordUtil;
import vn.edu.nuce.datn.util.SessionUtils;



@ManagedBean(name = "userProfileBean")
@SessionScoped
public class UserProfileBean extends BaseController implements Serializable {

	/**
	 * @author Nampv
	 */
	private static final long serialVersionUID = 9089358585644225640L;
	private SysUser user;
	private SysUser sysUserPass;
	private String password;
	private String newPassword;
	private String ReenterPassword;
	private String role;
	private SysRoleDAO sysRoleDAO;

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

	public void btnSavePassword() {
		if (validateSavePass()) {
			SysUserDAO userDao = new SysUserDAO();
//			sysUserPass = userDao.login(SessionUtils.getUserName(), PasswordUtil.generateHash(password));
			sysUserPass = userDao.findByUserName(SessionUtils.getUserName(), 0);
			if (sysUserPass != null && newPassword!= null && password != null) {
								
				if (newPassword.equals(password)) {
					super.showMessageWARN("common.save", " error ", "user.passUsed");
					return;
				} else {
					
					SysUserDAO sysUserDAO = new SysUserDAO();
//					String salt = PasswordUtil.getRandomSalt();
//					sysUserPass.setSalt(salt);
					sysUserPass.setPassword(PasswordUtil.generateHash(newPassword));
					sysUserDAO.saveOrUpdate(sysUserPass);
					this.showMessageINFO("common.save", " Change Password ");
					HttpSession session = SessionUtils.getSession();
					session.invalidate();
					ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
					HttpServletRequest req = (HttpServletRequest) ec.getRequest();
					try {
						ec.redirect(req.getContextPath() + "/login.xhtml");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "incorrect password!", "Please Try Again!"));
			}
		}

	}

	private boolean validateSavePass() {
		if (!PasswordUtil.validatePassword(newPassword)) {
			super.showMessageWARN("common.save", " Password ", "user.formatPassword");
			return false;
		}
		return true;
	}

}
