package vn.edu.nuce.datn.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  General Utilities
 *
 *  @author     huannn
 */
public class CommonUtil
{
	/**
	 *	Replace String values.
	 *  @param value string to be processed
	 *  @param oldPart old part
	 *  @param newPart replacement - can be null or ""
	 *  @return String with replaced values
	 */
	public static String replace (String value, String oldPart, String newPart)
	{
		if (value == null || value.length() == 0
			|| oldPart == null || oldPart.length() == 0)
			return value;
		//
		int oldPartLength = oldPart.length();
		String oldValue = value;
		StringBuilder retValue = new StringBuilder();
		int pos = oldValue.indexOf(oldPart);
		while (pos != -1)
		{
			retValue.append (oldValue.substring(0, pos));
			if (newPart != null && newPart.length() > 0)
				retValue.append(newPart);
			oldValue = oldValue.substring(pos+oldPartLength);
			pos = oldValue.indexOf(oldPart);
		}
		retValue.append(oldValue);
	//	log.fine( "Env.replace - " + value + " - Old=" + oldPart + ", New=" + newPart + ", Result=" + retValue.toString());
		return retValue.toString();
	}	//	replace
	
	/**
	 *	check phone String values.
	 *  @param value string to be processed
	 *  @param oldPart old part
	 *  @param newPart replacement - can be null or ""
	 *  @return String with replaced values
	 */
	public static String checkPhone (String value)
	{
		String ms=null;
		if(value==null || value.length()==0)
			return ms;
		if(value.length()>=21)
		{
			return ms;
		}
		for (int i = 0; i < value.length(); i++) {
			int j =  (int) value.charAt(i);
			char c=value.charAt(i);
			if(c=='*'| c=='('| c==')'| c==')'| c=='_'| c=='+'| c=='-'| c==' '| c=='.')
				;
			else if (48 > j || j > 57){
				
				return ms;
			}
		}
		boolean foundMatch = value.matches("~|!|@|#|$|%|^|&");
		if(foundMatch){
			return ms;
		}
		
		return ms;
	}	//	replace
	
	/**
	 * Remove CR / LF from String
	 * @param in input
	 * @return cleaned string
	 */
	public static String removeCRLF (String in)
	{
		char[] inArray = in.toCharArray();
		StringBuilder out = new StringBuilder (inArray.length);
		for (int i = 0; i < inArray.length; i++)
		{
			char c = inArray[i];
			if (c == '\n' || c == '\r')
				;
			else
				out.append(c);
		}
		return out.toString();
	}	//	removeCRLF

	/**
	 * Clean - Remove all white spaces
	 * @param in in
	 * @return cleaned string
	 */
	public static String cleanWhitespace (String in)
	{
		char[] inArray = in.toCharArray();
		StringBuilder out = new StringBuilder(inArray.length);
		boolean lastWasSpace = false;
		for (int i = 0; i < inArray.length; i++)
		{
			char c = inArray[i];
			if (Character.isWhitespace(c))
			{
				if (!lastWasSpace)
					out.append (' ');
				lastWasSpace = true;
			}
			else
			{
				out.append (c);
				lastWasSpace = false;
			}
		}
		return out.toString();
	}	//	cleanWhitespace

	/**
	 * Mask HTML content.
	 * i.e. replace characters with &values;
	 * CR is not masked
	 * @param content content
	 * @return masked content
	 * @see #maskHTML(String, boolean)
	 */
	public static String maskHTML (String content)
	{
		return maskHTML (content, false);
	}	//	maskHTML
	
	/**
	 * Mask HTML content.
	 * i.e. replace characters with &values;
	 * @param content content
	 * @param maskCR convert CR into <br>
	 * @return masked content or null if the <code>content</code> is null
	 */
	public static String maskHTML (String content, boolean maskCR)
	{
		// If the content is null, then return null - teo_sarca [ 1748346 ]
		if (content == null)
			return content;
		//
		StringBuilder out = new StringBuilder();
		char[] chars = content.toCharArray();
		for (int i = 0; i < chars.length; i++)
		{
			char c = chars[i];
			switch (c)
			{
				case '<':
					out.append ("&lt;");
					break;
				case '>':
					out.append ("&gt;");
					break;
				case '&':
					out.append ("&amp;");
					break;
				case '"':
					out.append ("&quot;");
					break;
				case '\'':
					out.append ("&#039;");
					break;
				case '\n':
					if (maskCR)
						out.append ("<br>");
				//
				default:
					int ii =  (int)c;
					if (ii > 255)		//	Write Unicode
						out.append("&#").append(ii).append(";");
					else
						out.append(c);
					break;
			}
		}
		return out.toString();
	}	//	maskHTML

	/**
	 * Get the number of occurances of countChar in string.
	 * @param string String to be searched
	 * @param countChar to be counted character
	 * @return number of occurances
	 */
	public static int getCount (String string, char countChar)
	{
		if (string == null || string.length() == 0)
			return 0;
		int counter = 0;
		char[] array = string.toCharArray();
		for (int i = 0; i < array.length; i++)
		{
			if (array[i] == countChar)
				counter++;
		}
		return counter;
	}	//	getCount

	/**
	 * Is String Empty
	 * @param str string
	 * @return true if >= 1 char
	 */
	public static boolean isEmpty (String str)
	{
		return isEmpty(str, false);
	}	//	isEmpty
	
	/**
	 * Is String Empty
	 * @param str string
	 * @param trimWhitespaces trim whitespaces
	 * @return true if >= 1 char
	 */
	public static boolean isEmpty (String str, boolean trimWhitespaces)
	{
		if (str == null)
			return true;
		if (trimWhitespaces)
			return str.trim().length() == 0;
		else
			return str.length() == 0;
	}	//	isEmpty
	
	/**************************************************************************
	 * Find index of search character in str.
	 * This ignores content in () and 'texts'
	 * @param str string
	 * @param search search character
	 * @return index or -1 if not found
	 */
	public static int findIndexOf (String str, char search)
	{
		return findIndexOf(str, search, search);
	}   //  findIndexOf

	/**
	 *  Find index of search characters in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search1 first search character
	 *  @param search2 second search character (or)
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, char search1, char search2)
	{
		if (str == null)
			return -1;
		//
		int endIndex = -1;
		int parCount = 0;
		boolean ignoringText = false;
		int size = str.length();
		while (++endIndex < size)
		{
			char c = str.charAt(endIndex);
			if (c == '\'')
				ignoringText = !ignoringText;
			else if (!ignoringText)
			{
				if (parCount == 0 && (c == search1 || c == search2))
					return endIndex;
				else if (c == ')')
						parCount--;
				else if (c == '(')
					parCount++;
			}
		}
		return -1;
	}   //  findIndexOf

	/**
	 *  Find index of search character in str.
	 *  This ignores content in () and 'texts'
	 *  @param str string
	 *  @param search search character
	 *  @return index or -1 if not found
	 */
	public static int findIndexOf (String str, String search)
	{
		if (str == null || search == null || search.length() == 0)
			return -1;
		//
		int endIndex = -1;
		int parCount = 0;
		boolean ignoringText = false;
		int size = str.length();
		while (++endIndex < size)
		{
			char c = str.charAt(endIndex);
			if (c == '\'')
				ignoringText = !ignoringText;
			else if (!ignoringText)
			{
				if (parCount == 0 && c == search.charAt(0))
				{
					if (str.substring(endIndex).startsWith(search))
						return endIndex;
				}
				else if (c == ')')
						parCount--;
				else if (c == '(')
					parCount++;
			}
		}
		return -1;
	}   //  findIndexOf

	
	/**************************************************************************
	 *  Return Hex String representation of byte b
	 *  @param b byte
	 *  @return Hex
	 */
	static public String toHex (byte b)
	{
		char hexDigit[] = {
			'0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
		};
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);
	}

	/**
	 *  Return Hex String representation of char c
	 *  @param c character
	 *  @return Hex
	 */
	static public String toHex (char c)
	{
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return toHex(hi) + toHex(lo);
	}   //  toHex
	
	/**************************************************************************
	 * Init Cap Words With Spaces
	 * @param in string
	 * @return init cap
	 */
	public static String initCap (String in)
	{
		if (in == null || in.length() == 0)
			return in;
		//
		boolean capitalize = true;
		char[] data = in.toCharArray();
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] == ' ' || Character.isWhitespace(data[i]))
				capitalize = true;
			else if (capitalize)
			{
				data[i] = Character.toUpperCase (data[i]);
				capitalize = false;
			}
			else
				data[i] = Character.toLowerCase (data[i]);
		}
		return new String (data);
	}	//	initCap

	
	/**************************************************************************
	 * Return a Iterator with only the relevant attributes.
	 * Fixes implementation in AttributedString, which returns everything
	 * @param aString attributed string
	 * @param relevantAttributes relevant attributes
	 * @return iterator
	 */
	static public AttributedCharacterIterator getIterator (AttributedString aString, 
		AttributedCharacterIterator.Attribute[] relevantAttributes)
	{
		AttributedCharacterIterator iter = aString.getIterator();
		Set<?> set = iter.getAllAttributeKeys();
	//	System.out.println("AllAttributeKeys=" + set);
		if (set.size() == 0)
			return iter;
		//	Check, if there are unwanted attributes
		Set<AttributedCharacterIterator.Attribute> unwanted = new HashSet<AttributedCharacterIterator.Attribute>(iter.getAllAttributeKeys());
		for (int i = 0; i < relevantAttributes.length; i++)
			unwanted.remove(relevantAttributes[i]);
		if (unwanted.size() == 0)
			return iter;

		//	Create new String
		StringBuilder sb = new StringBuilder();
		for (char c = iter.first(); c != AttributedCharacterIterator.DONE; c = iter.next())
			sb.append(c);
		aString = new AttributedString(sb.toString());

		//	copy relevant attributes
		Iterator<AttributedCharacterIterator.Attribute> it = iter.getAllAttributeKeys().iterator();
		while (it.hasNext())
		{
			AttributedCharacterIterator.Attribute att = it.next();
			if (!unwanted.contains(att))
			{
				for (char c = iter.first(); c != AttributedCharacterIterator.DONE; c = iter.next())
				{
					Object value = iter.getAttribute(att);
					if (value != null)
					{
						int start = iter.getRunStart(att);
						int limit = iter.getRunLimit(att);
					//	System.out.println("Attribute=" + att + " Value=" + value + " Start=" + start + " Limit=" + limit);
						aString.addAttribute(att, value, start, limit);
						iter.setIndex(limit);
					}
				}
			}
		//	else
		//		System.out.println("Unwanted: " + att);
		}
		return aString.getIterator();
	}	//	getIterator


	/**
	 * Is 8 Bit
	 * @param str string
	 * @return true if string contains chars > 255
	 */
	public static boolean is8Bit (String str)
	{
		if (str == null || str.length() == 0)
			return true;
		char[] cc = str.toCharArray();
		for (int i = 0; i < cc.length; i++)
		{
			if (cc[i] > 255)
			{
			//	System.out.println("Not 8 Bit - " + str);
				return false;
			}
		}
		return true;
	}	//	is8Bit
	
	/**
	 * Clean Ampersand (used to indicate shortcut) 
	 * @param in input
	 * @return cleaned string
	 */
	public static String cleanAmp (String in)
	{
		if (in == null || in.length() == 0)
			return in;
		int pos = in.indexOf('&');
		if (pos == -1)
			return in;
		//
		if (pos+1 < in.length() && in.charAt(pos+1) != ' ')
			in = in.substring(0, pos) + in.substring(pos+1);
		return in;
	}	//	cleanAmp
	
	/**
	 * Trim to max character length
	 * @param str string
	 * @param length max (incl) character length
	 * @return string
	 */
	public static String trimLength (String str, int length)
	{
		if (str == null)
			return str;
		if (length <= 0)
			throw new IllegalArgumentException("Trim length invalid: " + length);
		if (str.length() > length) 
			return str.substring(0, length);
		return str;
	}	//	trimLength
	
	/**
	 * Size of String in bytes
	 * @param str string
	 * @return size in bytes
	 */
	public static int size (String str)
	{
		if (str == null)
			return 0;
		int length = str.length();
		int size = length;
		try
		{
			size = str.getBytes("UTF-8").length;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return size;
	}	//	size

	/**
	 * Trim to max byte size
	 * @param str string
	 * @param size max size in bytes
	 * @return string
	 */
	public static String trimSize (String str, int size)
	{
		if (str == null)
			return str;
		if (size <= 0)
			throw new IllegalArgumentException("Trim size invalid: " + size);
		//	Assume two byte code
		int length = str.length();
		if (length < size/2)
			return str;
		try
		{
			byte[] bytes = str.getBytes("UTF-8");
			if (bytes.length <= size)
				return str;
			//	create new - may cut last character in half
			byte[] result = new byte[size];
			System.arraycopy(bytes, 0, result, 0, size);
			return new String(result, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return str;
	}	//	trimSize
	
	 /**
	 * Get int value
	 * @param value object
	 * @return int value or -1
	 */
	public static int getInt(Object value)
	{
		return getInt(value, -1);
	}
	
	 /**
	 * Get int value
	 * @param value object
	 * @return int value or -1
	 */
	public static int getInt(Object value, int defaultValue)
	{
		if (value == null)
			return defaultValue;

		if (value instanceof Integer)
			return ((Integer) value).intValue();
		if (value instanceof Long)
			return ((Long) value).intValue();
		if (value instanceof Double)
			return ((Double) value).intValue();
		if (value instanceof BigDecimal)
			return ((BigDecimal) value).intValue();

		try {
			Double ii = Double.parseDouble(value.toString());
			return ii.intValue();
		} catch (NumberFormatException ex) {
		}

		return defaultValue;
	}

	/**
	 * @author hungtq24
	 * @since 14/01/2014
	 * @param value
	 */
	public static boolean getBoolean(Object value)
	{
		return getBoolean(value, false);
	}
	
	/**
	 * Get boolean value
	 * @author hungtq24
	 * @since 14/01/2014
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static boolean getBoolean(Object value, boolean defaultValue)
	{
		if (value == null)
			return false;
		
		if (value instanceof Boolean)
			return ((Boolean)value).booleanValue();
		if (value instanceof String) {
			String val = ((String)value).toLowerCase();
			return "yes".equals(val) || "y".equals(val);
		}
			
		
		return defaultValue;
	}
	
	/**
	 * Convert date to string with format pattern
	 * @param value
	 * @param format
	 * @return
	 */
	public static String DateToString(Object value, String format) {
		if (value != null) {
			Date date = null;
			if (value instanceof Date)
				date = (Date) value;
			else if (value instanceof Timestamp)
				date = (Timestamp) value;
			else if (value instanceof String)
				date = Timestamp.valueOf((String) value);

			if (date != null) {
				SimpleDateFormat df = new SimpleDateFormat(format);

				return df.format(date);
			}
		}

		return "";
	}
	
	public static String format (BigDecimal value) {
		DecimalFormat df = new DecimalFormat();
		return value == null ? "" : df.format(value);
	}
	
	/**
	 * Check if is number
	 * @author hungtq24
	 * @date 13/02/2014
	 * @param s
	 * @return
	 */
	public static boolean isNumeric (String s) {
		return s.matches("[+-]?\\d*(\\.\\d+)?") 				//###.###
    			||s.matches("[+-]?\\d*(\\,\\d+)?")				//###,###
    			||s.matches("[+-]?\\d*(\\.\\d+)(\\,\\d+)?")		//#.###,###
    			||s.matches("[+-]?\\d*(\\,\\d+)(\\.\\d+)?"); 	//#,###.###
	}

	/***
	 * huannn: Kiem tra chuoi co ky tu dac biet
	 * @param inputStr
	 * @return
	 */
	public static boolean isContainSpecialCharacter(String inputStr) {
		
		if(inputStr == null) {
			return false;
		}		
		inputStr = inputStr.replace(" ", "");
		if("".equals(inputStr)) {
			return false;
		}
		
	    //Pattern p = Pattern.compile("[^.A-Za-z0-9]");
	    Pattern p = Pattern.compile("[~!@#$%^&*]");
	    Matcher m = p.matcher(inputStr);
	    boolean b = m.find();
	     
		return b;
	}

	public static Boolean CheckPress(String value) {
		for (int i = 0; i < value.trim().length(); i++) {
			char c = value.charAt(i);
			int j = (int) c;
			if ((j > 32 && j <= 46) || (j >= 58 && j <= 64)
					|| (j == 94 || j == 96) || j >= 123) {
				return false;
			}
		}
		return true;
	}
	
	public static BigDecimal getBigDecimal(String str) {
		try {
			
			return new BigDecimal(str);
		} catch (Exception ex) {
			
			return null;
		}
	}

	public static boolean checkNotNull(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			if (((String) obj).trim().length() == 0) {
				return false;
			}
		}
		return true;
	}

	public static boolean containsNumber(String c) {
		for (int i = 0; i < c.length(); ++i) {
			if (Character.isDigit(c.charAt(i)) == false)
				return false;
		}
		return true;
	}	
	
	private static Pattern pattern;
	private static Matcher matcher;

	private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

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
	
	public static String generateRandomChars(String baseChar, int length) {
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < length; i++) {
	        sb.append(baseChar.charAt(random.nextInt(baseChar.length())));
	    }

	    return sb.toString();
	}
}
