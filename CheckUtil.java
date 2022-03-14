package com;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.db.SQLClient;

public class CheckUtil {

	public static boolean isNull(String str) {
		if (str == null)
			return true;
		return false;
	}

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		str = str.trim();
		if (str.length() == 0)
			return true;
		return false;
	}

	public static boolean isNotEmpty(String str) {

		if (str == null)
			return false;
		str = str.trim();
		if (str.length() == 0)
			return false;
		return true;
	}

	public static boolean isAccount(String str) {

		if (isEmpty(str))
			return false;
		if (!isCharString(str))
			return false;
		if (str.length() < 6)
			return false;
		if (str.length() > 20)
			return false;

		return true;

	}

	public static boolean isPassword(String str) {

		if (isEmpty(str))
			return false;
		if (!isCharString(str))
			return false;
		if (str.length() < 6)
			return false;
		if (str.length() > 20)
			return false;
		return true;
	}

	public static boolean isMail(String str) {

		if (isEmpty(str))
			return false;
		String mailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		Boolean b = str.matches(mailRegex);

		return b;

	}

	public static boolean isNickname(String str) {

		if (isEmpty(str))
			return false;
		if (str.length() < 2)
			return false;
		if (str.length() > 50)
			return false;

		return true;

	}

	public static boolean isCharString(String str) {

		if (isEmpty(str))
			return false;
		return str.matches("[a-zA-Z0-9]*");

	}

	public static boolean isInteger(String str) {
		try {
			Integer.valueOf(str);
			return true;

		} catch (Exception e) {
		}

		return false;
	}

	public static boolean isNotInteger(String str) {
		return !isInteger(str);
	}

	public static boolean isDouble(String str) {

		try {
			Double.valueOf(str);
			return true;

		} catch (Exception e) {
		}

		return false;
	}

	public static boolean isNumeric(String str) {
		return str.matches("[0-9]*");

	}

	public static boolean isPositiveInteger(String str) { // 正数
		if (isNotInteger(str))
			return false;
		return str.matches("^([1-9][0-9]*)$");
	}

	public static boolean isDate(String str) {
		if (isEmpty(str))
			return false;
//		return str.matches("^\\d{4}(-|.|/)\\d{2}(-|.|/)\\d{2}$");
		return str.matches("^\\d{4}(-)\\d{2}(-)\\d{2}$");
	}

	public static boolean isDateYM(String str) {
		if (isEmpty(str))
			return false;
		return str.matches("^\\d{4}[0-1][0-9]$");
	}

	/**
	 * 在不为空的情况下同时不是int格式，返回true 否则false
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmptyAndNotInteger(String str) {
		if (isNotEmpty(str) && isNotInteger(str))
			return true;
		else
			return false;
	}

	// 将前台传来的数组格式的字符串转成List集合类型
	public static List<String> stringToList(String str) {
		List<String> list = new ArrayList<>();
		if (!CheckUtil.isEmpty(str)) {
			String[] arr = str.substring(1, str.length() - 1).trim().split(",");
			for (String string : arr) {
				if (CheckUtil.isNotEmpty(string.trim())) {
					list.add(string.trim());
				}
			}
		}
		return list;
	}

	public static void sqlToAddInParams(StringBuffer sql, SQLClient sqlClient, String s, String params) {
		if (isNotEmpty(params)) {
			sql.append(s);
			sql.append(" IN ( ");
			String[] paramsArr = params.split(",");
			for (int i = 0; i < paramsArr.length; i++) {
				if (i == paramsArr.length - 1) {
					sql.append(" ? )");
					sqlClient.addParameter(paramsArr[i]);
				} else {
					sql.append(" ? ,");
					sqlClient.addParameter(paramsArr[i]);
				}
			}
		}
	}

	public static String replaceIllegalChar(String target) {
		if(isNotEmpty(target)) {
			Pattern p = Pattern.compile("[.,，。&%#￥$@^\"\\?!:'*]");// 增加对应的标点
			Matcher m = p.matcher(target);
			String first = m.replaceAll("");
			p = Pattern.compile(" {2,}");// 去除多余空格
			m = p.matcher(first);
			target = first.replace(" ", "");
		}
		return target;
	}
	
	public static boolean isDateTime(String str) {
		if (isEmpty(str))
			return false;
		return str.matches("^\\d{4}(-)\\d{2}(-)\\d{2} \\d{2}(:)\\d{2}(:)\\d{2}$");
	}
	
	public static boolean isDateMonth(String str) {
		if (isEmpty(str))
			return false;
		return str.matches("^[1-2][0-9]{3}(-)(0[1-9])?(1[0-2])?$");
	}

	public static void main(String[] args) {

		System.out.println(isDateMonth("2019-12"));
	}
}
