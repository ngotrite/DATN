package vn.edu.nuce.datn.util;

import java.util.ResourceBundle;

public class ResourceBundleUtil {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.config");
	
	public static String getString(String key){
		return resourceBundle.getString(key);
	}
}
