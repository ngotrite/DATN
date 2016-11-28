package vn.edu.nuce.datn.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vn.edu.nuce.datn.dao.SysMenuDAO;
import vn.edu.nuce.datn.dao.SysUserDAO;
import vn.edu.nuce.datn.entity.SysMenu;
import vn.edu.nuce.datn.entity.SysUser;
import vn.edu.nuce.datn.util.PasswordUtil;
import vn.edu.nuce.datn.util.SessionUtils;


@ManagedBean(name = "userSession")
@SessionScoped
public class UserSession extends BaseController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Locale locale;
	private String userName;
	private String password;
	private SysUser sysUser;

	private SysUserDAO userDao;
	private SysMenuDAO menuDao;
	private List<SysMenu> lstRootMenu;

	public UserSession() {

		userDao = new SysUserDAO();
		menuDao = new SysMenuDAO();
		lstRootMenu = new ArrayList<>();
	}


	public void doLogin() throws IOException {
		
//		sysUser = userDao.login(userName, PasswordUtil.generateHash(password));
		lstRootMenu.clear();
		sysUser = userDao.findByUserName(userName, 0);
		boolean validate = false;
		if(sysUser != null) {
			
			String hash = PasswordUtil.generateHash(password, sysUser.getSalt());
			if(hash != null && hash.equals(sysUser.getPassword()))
				validate = true;
		}
		
		if (validate) {
			if (sysUser.getLanguague() != null) {
				locale = new Locale(sysUser.getLanguague());
			} else {
				locale = Locale.ENGLISH;
			}
			// Find menu not belong to user
			List<SysMenu> lstMenu = userDao.findAllMenuOfUser(sysUser.getId());
			List<SysMenu> lstMenuAll = menuDao.findAll(false);
			List<SysMenu> lstMenuRestrict = new ArrayList<SysMenu>();
			for (SysMenu menu : lstMenuAll) {

				boolean found = false;
				for (SysMenu menuAuth : lstMenu) {
					if (menu.getId() == menuAuth.getId()) {
						found = true;
						break;
					}
				}

				if (!found) {
					lstMenuRestrict.add(menu);
				}
			}

			// Build root menu nodes
			for (SysMenu menu : lstMenu) {

				if (menu.getParentId() == null)
					lstRootMenu.add(menu);
			}

			HttpSession session = SessionUtils.getSession();
			session.setAttribute(SessionUtils.SESSION_SYS_USER, sysUser);
			session.setAttribute(SessionUtils.SESSION_MENUS_RESTRICT, lstMenuRestrict);

			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			HttpServletRequest req = (HttpServletRequest) ec.getRequest();
			ec.redirect(req.getContextPath() + "/home.xhtml");
//			ec.redirect(req.getContextPath() + "/pages/student/graduation_period.xhtml");

			// return "home";
			//getLogger().info("LOGIN - " + userName);
		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!"));

			// return "login";
		}
	}
	

	public void doLogout() throws IOException {

		HttpSession session = SessionUtils.getSession();
		session.invalidate();
		lstRootMenu.clear();

		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest req = (HttpServletRequest) ec.getRequest();
		ec.redirect(req.getContextPath() + "/login.xhtml");
	}

	public String getDateFormat() {
		if (sysUser != null) {
			if (sysUser.getDateformat() != null) {
				return sysUser.getDateformat();
			}
		}
		return "yyyy-MM-dd";
	}

	public String getTimeFormat() {
		return getDateFormat() +" HH:mm:ss";
	}



	public void onSetLanguage(String lang) {

		setLanguage(lang);
	}

	/** GET - SET **/
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getLanguage() {
		return locale.getLanguage();
	}

	public String getDisplayLanguage() {
		return locale.getDisplayLanguage();
	}

	public void setLanguage(String language) {
		locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}



	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public List<SysMenu> getLstRootMenu() {
		return lstRootMenu;
	}

	public void setLstRootMenu(List<SysMenu> lstRootMenu) {
		this.lstRootMenu = lstRootMenu;
	}


}