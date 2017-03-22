package com.bbyiya.pic.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
* <p>Title:GetterUtil </p>
* <p>����: ������(field name��getter name��ת������)</p>
* <p>�汾:ASOS���� </p>
* @author PengHao
* @date 2016-7-29 ����5:59:28
 */
public class GetterUtil {
	
	public static String toGetter(String fieldname) {

		if (fieldname == null || fieldname.length() == 0) {
			return null;
		}

		if (fieldname.length() > 2) {
			String second = fieldname.substring(1, 2);
			if (!HasDigit(second) && second.equals(second.toUpperCase())) {
				return new StringBuffer("get").append(fieldname).toString();
			}
		}

		fieldname = new StringBuffer("get")
				.append(fieldname.substring(0, 1).toUpperCase())
				.append(fieldname.substring(1)).toString();

		return fieldname;
	}
	
	public static String toSetter(String fieldname) {

		if (fieldname == null || fieldname.length() == 0) {
			return null;
		}

		if (fieldname.length() > 2) {
			String second = fieldname.substring(1, 2);
			if (!HasDigit(second) && second.equals(second.toUpperCase())) {
				return new StringBuffer("set").append(fieldname).toString();
			}
		}

		fieldname = new StringBuffer("set")
				.append(fieldname.substring(0, 1).toUpperCase())
				.append(fieldname.substring(1)).toString();

		return fieldname;
	}
	
	// �ж�һ���ַ����Ƿ�������
	private static boolean HasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches()) {
			flag = true;
		}
		return flag;
	}
}

