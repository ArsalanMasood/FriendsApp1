package com.friends.friends;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	
	
	private static Pattern pattern;
	private static Matcher matcher;
	private static final String TIME_PATTERN= "^\\d{1,2}:\\d{1,2}$";//MM:SS
	private static final String CREDITCARD_EXP_DATE= "^\\d\\d/\\d\\d$";//MM/YY
	private static final String CREDITCARD_NUMBER= "[3,4,5,6][0-9]{15}";
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";
	//^(([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\-\.]+)@([a-zA-Z0-9_\-\.]+)\.([a-zA-Z]{2,5}){1,25})+)*$
	private static final String MOBILE_PATTERN2 = "[0-9]{10}";//XXXXXXXXX
	private static final String SSN_PATTERN =
	"[0-9]{3,}+-+[0-9]{2}+-+[0-9]{4}";//XXX-XX-XXXX
	private static final String MOBILE_PATTERN =
	"[0-9]{3,}+-+[0-9]{3}+-+[0-9]{4}";// XXX-XXX-XXXX
	 
	/**
	* Validate Email with regular expression
	* 
	* @param email
	*            Email for validation
	* @return true valid hex, false invalid hex
	*/
	public static boolean isValidEmail(final String email) {
	pattern = Pattern.compile(EMAIL_PATTERN);
	matcher = pattern.matcher(email);
	return matcher.matches();
	 
	}
	 
	/**
	* Validate Phone No with regular expression
	* 
	* @param phoneNo
	*            Phone No for validation
	* @return true valid Phone No, false invalid hex
	*/
	public static boolean isValidMobileNo(String phoneNo) {
	pattern = Pattern.compile(MOBILE_PATTERN2);
	matcher = pattern.matcher(phoneNo);
	return matcher.matches();
	 
	}
	public static boolean isValidTime(String time) {
	 pattern = Pattern.compile(TIME_PATTERN);
	 matcher = pattern.matcher(time);
	 return matcher.matches();
	 
	 }
	public static boolean isCreditCardExpDate(String time) {
	 pattern = Pattern.compile(CREDITCARD_EXP_DATE);
	 matcher = pattern.matcher(time);
	 return matcher.matches();
	 
	 }
	public static boolean isCreditCardNumber(String number) {
	 pattern = Pattern.compile(CREDITCARD_NUMBER);
	 matcher = pattern.matcher(number);
	 return matcher.matches();
	 
	 }
	/**
	* Validate Phone No with regular expression
	* 
	* @param phoneNo
	*            Phone No for validation
	* @return true valid Phone No, false invalid hex
	*/
	public static boolean isValidSSNo(String phoneNo) {
	pattern = Pattern.compile(SSN_PATTERN);
	matcher = pattern.matcher(phoneNo);
	return matcher.matches();
	 
	}

}
