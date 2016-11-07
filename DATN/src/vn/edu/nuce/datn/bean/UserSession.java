package vn.edu.nuce.datn.bean;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import vn.edu.nuce.datn.util.SessionUtils;

 
@ManagedBean(name="userSession")
@SessionScoped
public class UserSession implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Locale locale;
	private String userName;
	private String password;
	
	public UserSession() {
		
		locale = Locale.ENGLISH;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public String getLanguage() {
        return locale.getLanguage();
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
	
//	public void countryLocaleCodeChanged(ValueChangeEvent e){
//		
//		String newLocaleValue = e.getNewValue().toString();
//        for (Map.Entry<String, Object> entry : countries.entrySet()) {
//        
//        	if(entry.getValue().toString().equals(newLocaleValue)){
//        		
//        		FacesContext.getCurrentInstance()
//        			.getViewRoot().setLocale((Locale)entry.getValue());
//        		
//        	}
//        }
//	}

	public String getDateFormat() {
		if("vi".equals(locale.getLanguage())) {
    		return "dd/MM/yyyy";
    	} else {
    		return "yyyy-MM-dd";
    	}
	}

	public String doLogin() {
        //boolean result = UserDAO.login(uname, password);
		boolean result = true;
        if (result) {
            
            HttpSession session = SessionUtils.getSession();
            session.setAttribute("userName", userName);
            session.setAttribute("userId", 10000);//todo
 
            return "index";
        } else {
 
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Login!", "Please Try Again!"));
 
            // invalidate session, and redirect to other pages
 
            //message = "Invalid Login. Please Try Again!";
            return "login";
        }
    }
 
    public String doLogout() {
      HttpSession session = SessionUtils.getSession();
      session.invalidate();
      return "login";
   }
}