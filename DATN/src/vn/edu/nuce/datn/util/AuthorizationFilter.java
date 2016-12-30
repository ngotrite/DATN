package vn.edu.nuce.datn.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import vn.edu.nuce.datn.bean.UserSession;
import vn.edu.nuce.datn.entity.SysMenu;

@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml", "*.jsf" })
public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {

			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			HttpSession ses = reqt.getSession(false);
			UserSession userSession = (UserSession) reqt.getSession().getAttribute("userSession");

			String reqURI = reqt.getRequestURI();
			
			if(userSession != null && userSession.getSysUser() != null){ 
				if(reqURI.endsWith(reqt.getContextPath() + "/") || reqURI.endsWith(reqt.getContextPath() + "/home.xhtml")){
					resp.sendRedirect(reqt.getContextPath() + "/admin/document_ex.xhtml");
				}
			}
			
			if (reqURI.endsWith(reqt.getContextPath() + "/") || reqURI.endsWith(reqt.getContextPath() + "/home.xhtml") 
					|| reqURI.indexOf("/admin/login.xhtml") >= 0 || reqURI.indexOf("/errorRole.xhtml") >= 0
					|| reqURI.indexOf("/resources/") >= 0 || reqURI.contains("javax.faces.resource")) {

				chain.doFilter(request, response);
			} else if (ses != null && ses.getAttribute(SessionUtils.SESSION_SYS_USER) != null) {

				boolean auth = true;
				List<SysMenu> lstMenuRestrict = (List<SysMenu>) ses.getAttribute(SessionUtils.SESSION_MENUS_RESTRICT);
				if (lstMenuRestrict == null) {
					// Do nothing
				} else {

					String contextPath = reqt.getContextPath();
					if (contextPath.equals(reqURI) || (contextPath + "/").equals(reqURI)
							|| reqURI.indexOf("/home.xhtml") >= 0) {
						// Do nothing
					} else {

						// Todo: Not elegant
						if ("GET".equals(reqt.getMethod())) {
							// Chi han che truy cap doi voi cac menu da duoc
							// khai bao va khong duoc assign cho user

							String treeType = reqt.getParameter("treeType");
							for (SysMenu menuRestrict : lstMenuRestrict) {

								if (CommonUtil.isEmpty(menuRestrict.getUrl()) || "/".equals(menuRestrict.getUrl())
										|| "/home.xhtml".equals(menuRestrict.getUrl()))
									continue;

								if (!CommonUtil.isEmpty(treeType)) {
									if (menuRestrict.getUrl() != null && menuRestrict.getUrl().contains(treeType)) {
										auth = false;
										break;
									}
								} else if (reqURI.indexOf(menuRestrict.getUrl()) >= 0) {
									auth = false;
									break;
								}
							}
						}
					}
				}

				if (auth) {
					chain.doFilter(request, response);
				} else {
					resp.sendRedirect(reqt.getContextPath() + "/errorRole.xhtml");
				}
			} else {

				if (isAJAXRequest(reqt) && reqURI.indexOf("/home.xhtml") == -1) {

					StringBuilder sb = new StringBuilder();
					sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"")
							.append(reqt.getContextPath() + "/admin/login.xhtml").append("\"></redirect></partial-response>");
					resp.setHeader("Cache-Control", "no-cache");
					resp.setCharacterEncoding("UTF-8");
					resp.setContentType("text/xml");
					PrintWriter pw = response.getWriter();
					pw.println(sb.toString());
					pw.flush();
					return;
				}
				
				if (reqURI.indexOf("home.xhtml;jsessionid") > 0){
					resp.sendRedirect(reqt.getContextPath() + "/");
				}
				if (reqURI.indexOf("/admin") > 0){
					resp.sendRedirect(reqt.getContextPath() + "/admin/login.xhtml");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isAJAXRequest(HttpServletRequest request) {

		boolean check = false;
		String facesRequest = request.getHeader("Faces-Request");
		if (facesRequest != null && facesRequest.equals("partial/ajax")) {
			check = true;
		}
		return check;
	}

	@Override
	public void destroy() {

	}
}