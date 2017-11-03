package com.bbyiya.utils.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.bbyiya.utils.encrypt.MD5Encrypt;

public class WxUtil {

	/**
	 * 时间串
	 * 
	 * @return
	 */
	public static long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 随机字符串
	 * 
	 * @return
	 */
	public static String genNonceStr() {
		Random random = new Random();
		return MD5Encrypt.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes()).toUpperCase();
	}

	// 请求方法
	public static String httpsRequest(String requestUrl, String outputStr) {
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod("POST");
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				OutputStream outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			// 释放资源
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			conn.disconnect();
			return buffer.toString();
		} catch (ConnectException ce) {
			// System.out.println("连接超时：{}"+ ce);
		} catch (Exception e) {
			// System.out.println("https请求异常：{}"+ e);
		}
		return null;
	}

	/**
	 * xmlStr 转化成map
	 * 
	 * @param xml
	 * @return
	 */
	public static Map<String, Object> xml2Map(String xml) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			// String reuslt=root.elementText("return_code");

			List<Element> list = root.elements();
			if (list != null && list.size() > 0) {
				for (Element element : list) {
					map.put(element.getName(), element.getText());
				}
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SortedMap<String, String> xmlToMap(String xml) {
		try {
			SortedMap<String, String> sortedMap = new TreeMap<String, String>();
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();

			List<Element> list = root.elements();
			if (list != null && list.size() > 0) {
				for (Element element : list) {
					sortedMap.put(element.getName(), element.getText());
				}
			}
			return sortedMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将参数打包
	 * 
	 * @param params
	 * @return
	 */
	private static String getPackage(List<NameValuePair> params) {

		Collections.sort(params, new Comparator<NameValuePair>() {
			// 重写排序规则
			public int compare(NameValuePair list1, NameValuePair list2) {
				return list1.getName().compareTo(list2.getName());

			}
		});
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			if (i != params.size() - 1)
				sb.append('&');
		}
		return sb.toString();
	}

	public static List<NameValuePair> bean2Parameters(Object bean) {
		if (bean == null) {
			return null;
		}
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();

		// 取得bean所有public 方法
		Method[] Methods = bean.getClass().getMethods();
		for (Method method : Methods) {
			if (method != null && method.getName().startsWith("get") && !method.getName().startsWith("getClass")) {
				// 得到属性的类名
				String value = "";
				// 得到属性值
				try {
					String className = method.getReturnType().getSimpleName();
					if (className.equalsIgnoreCase("int")) {
						int val = 0;
						try {
							val = (Integer) method.invoke(bean);
						} catch (InvocationTargetException e) {
						}
						value = String.valueOf(val);
					} else if (className.equalsIgnoreCase("String")) {
						try {
							value = (String) method.invoke(bean);
						} catch (InvocationTargetException e) {
						}
					}
					if (value != null && value != "") {
						// 添加参数
						// 将方法名称转化为id，去除get，将方法首字母改为小写
						String param = method.getName().replaceFirst("get", "");
						if (param.length() > 0) {
							String first = String.valueOf(param.charAt(0)).toLowerCase();
							param = first + param.substring(1);
						}
						parameters.add(new BasicNameValuePair(param, value));
					}
				} catch (IllegalArgumentException e) {

				} catch (IllegalAccessException e) {

				}
			}
		}
		return parameters;
	}

	/**
	 * 将对象序列化成 a=ss&b=bb&c=cc有序字符串
	 * 
	 * @param order
	 * @return
	 */
	public static String sortParam(Object order) {
		List<NameValuePair> list = bean2Parameters(order);
		return getPackage(list);
	}

	/**
	 * 获取xml String
	 * 
	 * @param params
	 * @return
	 */
	public static String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");
			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 验证签名
	 * 
	 * @param paramMap
	 * @param key
	 * @return
	 */
	public static boolean isWXsign(SortedMap<String, String> paramMap, String key) {
		StringBuffer sb = new StringBuffer();
		String checkSign = "";
		Set es = paramMap.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
			if ("sign".equals(k)) {
				checkSign = v;
			}
		}
		sb.append("key=" + key);
		String sign = MD5Encrypt.encrypt(sb.toString()).toUpperCase();

		return sign.equals(checkSign);
	}

}
