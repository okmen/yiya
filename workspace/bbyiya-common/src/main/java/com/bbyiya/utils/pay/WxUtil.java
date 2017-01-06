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
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.sdicons.json.validator.impl.predicates.Str;

public class WxUtil {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	public static String httpPost(String urlStr, String param) {
		String result = "";
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write(new String(param.getBytes("ISO-8859-1"))); // param.getBytes(),"ISO-8859-1"
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				result += line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	//请求方法  
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
              System.out.println("连接超时：{}"+ ce);  
          } catch (Exception e) {  
              System.out.println("https请求异常：{}"+ e);  
          }  
          return null;  
    }  

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
}
