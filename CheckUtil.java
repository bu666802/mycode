package com;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckUtil {
	
	public static boolean isNull(String str){
		if(str==null)return true;
		else return false;
	}
	
	public  static boolean isEmpty(String str){
		if(str==null)return true;
		str = str.trim();
		if(str.length()==0)return true;
		return false;
	}
	

	public  static boolean isNotEmpty(String str){
		if(str==null)return false;
		str = str.trim();
		if(str.length()==0)return false;
		return true;
	}
	
	
	public static boolean isAccount(String str){
		if(isEmpty(str))return false;
		if(!isCharString(str))return false;
		if(str.length()<3) return false;
		if(str.length()>32) return false;
		return true ;
		
	}
	
	public static boolean isPassword(String str) {
		if(isEmpty(str))return false;
		if(!isCharString(str))return false;
		if(str.length()<6) return false;
		if(str.length()>20) return false;
		return true ;
	} 
	
	public static String trimAll(String str) {
		str = str.trim();
		str = str.replaceAll(" ", "");
		return str ;
	}
	
	/**
	 * 
	 * @param str
	 * @param min 若没有最小限制，则 min = 0
	 * @param max 若没有最大限制，则 max = 0
	 * @return
	 */
	public static boolean checkStrLength(String str,int min,int max) {
		if(isNull(str)) return false;
		if(str.length()<min) return false;
		
		if(max!=0 && str.length()>max) return false;
		return true ;
	}
	
	public static void main(String[] args) {
		String str = "185  5           00 1";
		if(CheckUtil.isNotEmpty(str)) {
			str = str.replaceAll(" ","");
		}
		System.out.println(isPassword(str));
	}
	
	
	public static boolean isMail(String str) {
		if(isEmpty(str))return false;
		String mailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		Boolean b = str.matches(mailRegex);

		return b;

	}
	
	public static boolean isNickname(String str) {
		if(isEmpty(str))return false;
		if(str.length()<2) return false;
		if(str.length()>50) return false;
		return true;
	}

	public  static boolean isCharString(String str){
		if(isEmpty(str))return false;
		return str.matches("[a-zA-Z0-9]*") ;
	}
	
	
	
	public  static boolean isInteger(String str){
		try {
			Integer.valueOf(str);
			return true;
		} catch (Exception e) {}
		return false;
	}
	
	public static boolean isNotInteger(String str){
		return !isInteger(str);
	}
	
	public static boolean isDouble(String str){
		try {
			Double.valueOf(str);
			return true;
		} catch (Exception e) {}
		return false;
	}
	
	public static boolean isNotDouble(String str){
		return !isDouble(str);
	}
	
	public static boolean isNumeric(String str) {
		return str.matches("[0-9]*") ;
	}
	
	public static boolean isNotNumeric(String str){
		return !isNumeric(str);
	}
	

	
	/**
	 * 年月日
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str){
		if(isEmpty(str))return false;
//		return str.matches("^\\d{4}(-|.|/)\\d{2}(-|.|/)\\d{2}$");
		return str.matches("^\\d{4}(-)\\d{2}(-)\\d{2}$");
	}
	
	/**
	 * 年月日
	 * @param str
	 * @return
	 */
	public static boolean isNotDate(String str){
		return !isDate(str);
	}
	
	/**
	 * 年月
	 * @param str
	 * @return
	 */
	public static boolean isDateYM(String str){
		if(isEmpty(str))return false;
		return str.matches("^\\d{4}[0-1][0-9]$");
	}
	
	public static boolean isNotDateYM(String str){
		return !isDateYM(str);
	}
	
	
	/**
	 * 不为空同时又不是int格式，返回true
	 * 其它情况返回false
	 * @param str
	 * @return
	 */
	public static boolean isNotEmptyAndNotInteger(String str){
		if(isNotEmpty(str) && isNotInteger(str))return true;
		else return false;
	}
	
	
	public static boolean isNotEmptyAndNotDate(String str){
		if(isNotEmpty(str) && isNotDate(str))return true;
		else return false;
	}
	
	public static boolean isNotEmptyAndNotDateYM(String str){
		if(isNotEmpty(str) && isNotDateYM(str))return true;
		else return false;
	}
	
	public static boolean endsWith(String photo){
		boolean boo = photo.endsWith(".png")||photo.endsWith(".jpg");
		return boo;
	}
	
	/**
	 * 判断是手机号码，11位数，第一个数字为1
	 * @param str
	 * @return
	 */
	public static boolean isActionPhone(String str){
		if(isEmpty(str))return false;
		return str.matches("^1[0-9]{10}$");
	}
	
	public static String zerofill(String str,int len){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String codeTime     = df.format(new Date());
		int strLength = str.length();
		String zeroStr = "";
		if(strLength<len){
			for(int i=0; i<(len-strLength); i++){
				zeroStr = "0"+zeroStr;
			}
		}
		str = codeTime+zeroStr+str;
		return str;
	}

}
