package vn.edu.nuce.datn.util;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import vn.edu.nuce.datn.entity.SysUser;


public class SessionUtils {
	
	public static final String SESSION_SYS_USER = "SESSION_SYS_USER";
//	public static final String SESSION_MENUS = "SESSION_MENUS";
	public static final String SESSION_MENUS_RESTRICT = "SESSION_MENUS_RESTRICT";

	public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}

	public static HttpServletRequest getRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public static SysUser getUser() {
		
		HttpSession session = getSession();
		if(session == null)
			return null;
		
		return (SysUser) session.getAttribute(SESSION_SYS_USER);
	}
	
	public static String getUserName() {
		
		SysUser sysUser = getUser();
		if(sysUser == null)
			return null;
		
		return sysUser.getUserName();
	}

	public static String getClientIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null || ip.length() == 0) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		return ip;
	}
}