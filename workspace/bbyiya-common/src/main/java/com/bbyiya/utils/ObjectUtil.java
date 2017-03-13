package com.bbyiya.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ObjectUtil {
	/**
	 * @Title:isEmpty
	 * @Description:集合是否为空
	 * @param @param s
	 * @param @return
	 * @return boolean-
	 * @throws
	 * @author xiehz
	 * @date 2015年4月27日
	 */
	public static boolean isEmpty(Collection<?> s) {
		if (null == s) {
			return true;
		}
		return s.isEmpty();
	}

	/**
	 * @Title:isEmpty
	 * @Description:map是否为空
	 * @param @param s
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xiehz
	 * @date 2015年4月27日
	 */
	public static boolean isEmpty(Map<?, ?> s) {
		if (null == s) {
			return true;
		}
		return s.isEmpty();
	}

	/**
	 * @Title:isEmpty
	 * @Description:字符串是否为空
	 * @param @param s
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xiehz
	 * @date 2015年4月27日
	 */
	public static boolean isEmpty(String s) {
		if (null == s) {
			return true;
		}
		return s.toString().trim().length() <= 0;
	}

	/**
	 * @Title:isEmpty
	 * @Description:对象是否为空
	 * @param @param s
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xiehz
	 * @date 2015年4月27日
	 */
	public static <T> boolean isEmpty(T s) {
		if (null == s) {
			return true;
		}
		return false;

	}

	/**
	 * @Title:isEmpty
	 * @Description:对象是否为空
	 * @param @param s
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xiehz
	 * @date 2015年4月27日
	 */
	public static <T> boolean isEmpty(T[] s) {
		if (null == s) {
			return true;
		}
		return Array.getLength(s) < 1;
	}

	/**
	 * @Title:isNotEmpty
	 * @Description:集合不为空
	 * @param @param s
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xiehz
	 * @date 2015年4月27日
	 */
	public static boolean isNotEmpty(Collection<?> s) {
		if (null == s) {
			return false;
		}
		return !s.isEmpty();
	}

	/**
	 * @Title:isNotEmpty
	 * @Description:map不为空
	 * @param @param s
	 * @param @return
	 * @return boolean
	 * @throws
	 * @author xiehz
	 * @date 2015年4月27日
	 */
	public static boolean isNotEmpty(Map<?, ?> s) {
		if (null == s) {
			return false;
		}
		return !s.isEmpty();
	}

	/**
	 * 字符串不为空 ***********************************
	 * 
	 * @author xiehz
	 * @create_date 2013-10-11 下午10:03:09
	 * @param t
	 * @return ***********************************
	 */
	public static boolean isNotEmpty(String s) {
		if (null == s) {
			return false;
		}
		return s.toString().trim().length() > 0;
	}

	/**
	 * int 大于等于0 ***********************************
	 * 
	 * @author xiehz
	 * @create_date 2013-10-11 下午10:03:09
	 * @param t
	 * @return ***********************************
	 */
	public static boolean isNotEmpty(Integer s) {
		if (null == s) {
			return false;
		}
		return s >= 0;
	}

	/**
	 * 对象是否为空 ***********************************
	 * 
	 * @author xiehz
	 * @create_date 2013-10-11 下午10:03:09
	 * @param t
	 * @return ***********************************
	 */
	public static <T> boolean isNotEmpty(T s) {
		if (null == s) {
			return false;
		}
		return !isEmpty(s);
	}

	/**
	 * 转换list为 id列表 ***********************************
	 * 
	 * @author xiehz
	 * @create_date 2013-10-11 下午10:03:18
	 * @param t
	 * @return ***********************************
	 */
	public static <T> String listToString(Collection<T> t, String keyName) {
		String methodName = "";
		StringBuilder keys = new StringBuilder();
		try {
			for (T t2 : t) {
				methodName = "get" + keyName.substring(0, 1).toUpperCase() + keyName.substring(1);
				Method method = t2.getClass().getDeclaredMethod(methodName);
				Object res = method.invoke(t2);
				if (null != res) {
					keys.append(res);
					keys.append(",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (keys.length() > 0) {
			return keys.substring(0, keys.length() - 1);
		} else {
			return "";
		}
	}

	/**
	 * 转换list为 id列表 ***********************************
	 * 
	 * @author xiehz
	 * @create_date 2013-10-11 下午10:03:18
	 * @param t
	 * @return ***********************************
	 */
	public static <T> String arrayToString(T[] t) {
		StringBuilder keys = new StringBuilder();
		for (T t2 : t) {
			keys.append(t2);
			keys.append(",");
		}
		if (keys.length() > 0) {
			return keys.substring(0, keys.length() - 1);
		} else {
			return "";
		}
	}

	/**
	 * 转换list为 id列表 ***********************************
	 * 
	 * @author xiehz
	 * @create_date 2013-10-11 下午10:03:18
	 * @param t
	 * @return ***********************************
	 */
	public static <T> String listToString(Collection<T> t) {
		StringBuilder keys = new StringBuilder();
		for (T t2 : t) {
			keys.append(t2);
			keys.append(",");
		}
		if (keys.length() > 0) {
			return keys.substring(0, keys.length() - 1);
		} else {
			return "";
		}
	}

	public static String toFirstLetterUpperCase(String str) {
		if (str == null || str.length() < 2) {
			return str;
		}
		String firstLetter = str.substring(0, 1).toUpperCase();
		return firstLetter + str.substring(1, str.length());
	}

	/**
	 * 整型转换为4位字节数组
	 * 
	 * @author xiehz
	 * @create_date 2015-1-27 下午5:11:58
	 * @param intValue
	 * @return
	 */
	public static byte[] int2Byte(int intValue) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
			// System.out.print(Integer.toBinaryString(b[i])+" ");
			// System.out.print((b[i] & 0xFF) + " ");
		}
		return b;
	}

	/**
	 * 4位字节数组转换为整型
	 * 
	 * @author xiehz
	 * @create_date 2015-1-27 下午5:11:47
	 * @param b
	 * @return
	 */
	public static int byte2Int(byte[] b) {
		int intValue = 0;
		// int tempValue = 0xFF;
		for (int i = 0; i < b.length; i++) {
			intValue += (b[i] & 0xFF) << (8 * (3 - i));
			// System.out.print(Integer.toBinaryString(intValue)+" ");
		}
		return intValue;
	}

	/**
	 * @author xiehz
	 * @create_date 2014-8-7 上午10:16:59
	 * @param score
	 * @return
	 */
	public static Float parseFloat(String score) {
		if (isNotEmpty(score)) {
			if (isDouble(score)) {
				return Float.valueOf(score);
			}
		}
		return 0f;
	}

	/**
	 * @author xiehz
	 * @create_date 2014-8-7 上午10:16:59
	 * @param score
	 * @return
	 */
	public static Integer parseInt(String score) {
		if (isNotEmpty(score)) {
			if (isDouble(score)) {
				return Integer.valueOf(score);
			}
		}
		return 0;
	}

	public static Long parseLong(String score) {
		if (isNotEmpty(score)) {
			if (isDouble(score)) {
				return Long.valueOf(score);
			}
		}
		return 0l;
	}

	public static final Pattern integerPattern = Pattern.compile("^[-\\+]?[\\d]*$");

	/**
	 * 
	 * @author xiehz
	 * @create_date 2014-8-7 上午10:23:15
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		return integerPattern.matcher(str).matches();
	}

	/*
	 * 
	 * @param str
	 * 
	 * @return
	 */
	public static final Pattern floatPattern = Pattern.compile("^[-\\+]?[.\\d]*$");

	/**
	 * 判断是否为浮点数，包括double和float
	 * 
	 * @author xiehz
	 * @create_date 2014-8-7 上午10:22:54
	 * @param str传入的字符串
	 * @return是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		return floatPattern.matcher(str).matches();
	}

	/**
	 * @author xiehz
	 * @create_date 2014-8-8 上午11:26:33
	 * @param difficulty
	 * @return
	 */
	public static byte stringToByte(String difficulty) {

		if (ObjectUtil.isNotEmpty(difficulty)) {
			if (difficulty.length() == 1) {
				return Byte.valueOf(difficulty);
			}
		}
		return (byte) 0;
	}

	/**
	 * @author xiehz
	 * @create_date 2014-9-1 下午5:26:29
	 * @param paperIdSb
	 * @return
	 */
	public static String setToString(Set<Integer> set) {
		if (isEmpty(set)) {
			return "";
		}
		String ids = set.toString();
		return ids.substring(1, ids.length() - 1);
	}

	/**
	 * 判断 a 是否在【a,b,c】集合中
	 * 
	 * @author xiehz
	 * @create_date 2015-1-27 下午5:12:27
	 * @param org
	 * @param compArray
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean isIn(T org, T... compArray) {
		for (T t : compArray) {
			if (t.equals(org)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 数字和字母混合
	 */
	public static final Pattern numberAlphaPattern = Pattern.compile("^[A-Za-z0-9]+$");

	public static boolean isNumberAlphaFix(String str) {
		return numberAlphaPattern.matcher(str).matches();
	}

	/**
	 * 
	 * @Title: getPropertyValue
	 * @Description: 读取实体bean属性值
	 * @param @param bean
	 * @param @param propertyName
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@SuppressWarnings("finally")
	public static Object getPropertyValue(Object bean, String propertyName) {
		Object result = null;
		if (propertyName.equals("serialVersionUID")) {
			return result;
		}
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, bean.getClass());
			Method m = pd.getReadMethod();
			result = m.invoke(bean);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			return result;
		}
	}

	/**
	 * 
	 * @Title: setProperty
	 * @Description: 设置实体bean的属性值
	 * @param @param bean
	 * @param @param propertyName
	 * @param @param value
	 * @return void
	 * @throws
	 */
	public static void setProperty(Object bean, String propertyName, Object value) {
		PropertyDescriptor pd;
		try {
			pd = new PropertyDescriptor(propertyName, bean.getClass());
			Method m = pd.getWriteMethod();
			// 设置属性值
			m.invoke(bean, value);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将父类 值cope到子类 父类属性 需public
	 * 
	 * @param father
	 * @param child
	 * @return
	 * @throws Exception
	 */
	public static Object fatherToChild(Object father, Object child) throws Exception {
		if (!(child.getClass().getSuperclass() == father.getClass())) {
			throw new Exception("child不是father的子类");
		}
		@SuppressWarnings("rawtypes")
		Class fatherClass = father.getClass();
		Field ff[] = fatherClass.getDeclaredFields();
		for (int i = 0; i < ff.length; i++) {
			Field f = ff[i];// 取出每一个属性，如deleteDate
			@SuppressWarnings({ "unused", "rawtypes" })
			Class type = f.getType();
			String nameString = upperHeadChar(f.getName());
			if (!"SerialVersionUID".equals(nameString)) {
				@SuppressWarnings("unchecked")
				Method m = fatherClass.getMethod("get" + upperHeadChar(f.getName()));// 方法getDeleteDate
				Object obj = m.invoke(father);// 取出属性值
				f.set(child, obj);
			}
		}
		return child;
	}

	/**
	 * 首字母大写，in:deleteDate，out:DeleteDate
	 */
	private static String upperHeadChar(String in) {
		String head = in.substring(0, 1);
		String out = head.toUpperCase() + in.substring(1, in.length());
		return out;
	}

	// 过滤 ‘
	// ORACLE 注解 -- /**/
	// 关键字过滤 update ,delete
	static String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|" + "(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";

	static Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);

	/***************************************************************************
	 * 参数校验 URLDecoder.decode(param,"UTF-8")
	 * 
	 * @param str  
	 * @return true没有危险，false有风险
	 */
	public static boolean validSqlStr(String str) {
		if (sqlPattern.matcher(str).find()) {
			// logger.error("未能通过过滤器：p=" + p);
			return false;
		}
		return true;
	}

	/**
	 * 有空格
	 * 
	 * @param s
	 * @return
	 */
	public static boolean hasKongge(String s) {
		int i = s.indexOf(" ");
		if (i == -1)
			return true;
		return false;
	}
	
	/**
	 * URL参数解码
	 * @param paramStr
	 * @param enc 解码方式 默认 utf-8
	 * @return
	 */
	public static String urlDecoder_decode(String paramStr ,String enc){
		try {
			return URLDecoder.decode(paramStr,isEmpty(enc)?"UTF-8":enc); 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return paramStr;
		}
	}
	
	
	/**
	 * 手机号码验证
	 * 
	 * @param phoneNum
	 * @return
	 */
	public static boolean isMobile(String mobiles){  
		  Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		  Matcher m = p.matcher(mobiles);  
		  return m.matches();  
	}  
}
