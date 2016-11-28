package vn.edu.nuce.datn.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


@FacesValidator("ValidatorUsername")
public class ValidatorUsername implements Validator{

	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,20}$";

	private Pattern pattern;
	private Matcher matcher;

	public ValidatorUsername(){
		  pattern = Pattern.compile(USERNAME_PATTERN);
	}

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		
		matcher = pattern.matcher(value.toString());
		if(!matcher.matches()){

			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			FacesMessage msg =new FacesMessage(LocaleUtils.getString(locale, "common.validationFail"), LocaleUtils.getString(locale, "user.formatUserName"));
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);

		}
	}
}