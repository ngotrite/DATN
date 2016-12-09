package vn.edu.nuce.datn.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {


	private static Pattern pattern;
	private static Matcher matcher;

//	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";
	private static final String PASSWORD_PATTERN = ".*";

	/**
	 * Validate password with regular expression
	 * 
	 * @param password
	 *            password for validation
	 * @return true valid password, false invalid password
	 */
	public static boolean validatePassword(String password) {
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);
		return matcher.matches();
	}
	
	public static String getRandomSalt() {
		
		return UUID.randomUUID().toString();
	}
	
	public static String generateHash(String rawPass, String salt) {
		
		if(CommonUtil.isEmpty(rawPass))
			return null;
		if(CommonUtil.isEmpty(salt))
			salt = "Huannn123";
		
		try {
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update((rawPass + salt).getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(String.format("%02x", b & 0xff));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
