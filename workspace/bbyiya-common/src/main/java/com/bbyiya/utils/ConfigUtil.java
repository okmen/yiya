package com.bbyiya.utils;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Configuration;

/**
 * 获取配置信息 配置文件 config.xml
 * 
 * @author Administrator
 *
 */
public class ConfigUtil {
	/**
	 * 配置文件名
	 */
	private static final String FILE_NAME = "config.xml";

	private ConfigUtil() {
	}

	/**
	 * 获取配置文件 commons.property 参数值
	 * @param key
	 * @return
	 */
	public static String getPropertyVal(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("commons");// 获取当前的域名
		if (bundle.containsKey(key))
			return bundle.getString(key);
		return null;
	}
	
	/**
	 * 获取config.xml的某个元素的值（默认为属性value的值）
	 * 
	 * @param arg
	 * @return
	 */
	public static String getSingleValue(String arg) {
		String result = getPropertyVal(arg); 
		if(!ObjectUtil.isEmpty(result))
			return result;
		try {
			URL url = Configuration.class.getClassLoader().getResource(FILE_NAME);
			String str = url.getFile();
			// 转换编码
			str = URLDecoder.decode(str, "utf-8");
			File conf = new File(str);
			SAXReader reader = new SAXReader();
			Document document = reader.read(conf);
			Element root = document.getRootElement();
			Element chird = root.element(arg);
			if (chird != null)
				result = chird.attributeValue("value");
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	/**
	 * 获取appSetting.xml的某个元素的属性值
	 * 
	 * @param arg
	 * @param attributeName
	 * @return
	 */
	public static String getSingleValue(String arg, String attributeName) {
		String result = "";
		try {
			URL url = Configuration.class.getClassLoader().getResource(FILE_NAME);
			String str = url.getFile();
			// 转换编码
			str = URLDecoder.decode(str, "utf-8");
			File conf = new File(str);
			SAXReader reader = new SAXReader();
			Document document = reader.read(conf);
			Element root = document.getRootElement();
			Element chird = root.element(arg);
			if (chird != null)
				result = chird.attributeValue(attributeName);
		} catch (Exception e) {
			// TODO: handle exception

		}
		return result;
	}

	/**
	 * 根据父节点，获取子节点列表
	 * 
	 * @param parentNodeName
	 * @return
	 */
	public static List<Map<String, String>> getMaplist(String parentNodeName) {
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		try {
			URL url = Configuration.class.getClassLoader().getResource(FILE_NAME);
			String str = url.getFile();
			// 转换编码
			str = URLDecoder.decode(str, "utf-8");
			File conf = new File(str);
			SAXReader reader = new SAXReader();
			Document document = reader.read(conf);
			Element root = document.getRootElement();
			Element parent = root.element(parentNodeName);
			if (parent != null) {
				List<Element> list = parent.elements();
				if (list != null && list.size() > 0) {
					for (Element ee : list) {
						Map<String, String> map = new HashMap<String, String>();

						for (int i = 0; i < ee.attributes().size(); i++) {
							org.dom4j.Attribute aa = (org.dom4j.Attribute) ee.attributes().get(i);
							map.put(aa.getName(), aa.getValue());
						}
						maplist.add(map);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return maplist;
	}

}
