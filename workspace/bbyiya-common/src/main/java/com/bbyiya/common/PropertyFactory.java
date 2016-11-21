package com.bbyiya.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class PropertyFactory {
	private static final Log logger = LogFactory.getLog(PropertyFactory.class);

	private static Map<String, Properties> propMap = null;

	/**
	 * 
	 * @Title: getProperty
	 * @Description: 获取属性数据
	 * @param @param filePath
	 * @param @param name
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String getProperty(String filePath, String name) {
		if (propMap == null) {
			propMap = new HashMap<String, Properties>();
		}
		String key = getFileNameFromPath(filePath);
		if (!propMap.containsKey(key)) {
			Properties prop = new Properties();
			try {
				InputStream is = new BufferedInputStream(new FileInputStream(filePath));
				/*InputStream in = PropertyFactory.class.getResourceAsStream("/"+key);*/
				prop.load(is);
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				logger.error(e.getStackTrace());
				return null;
			}
			propMap.put(key, prop);
			return prop.getProperty(name);
		}
		return propMap.get(key).getProperty(name);
	}

	public static String getPropertyValue(String fileName, String key) {
		ResourceBundle bundle = ResourceBundle.getBundle(fileName);
		return bundle.getString(key);
	}

	/**
	 * 
	 * @Title: getFileNameFromPath
	 * @Description: 根据路径解析文件名
	 * @param @param path
	 * @param @return
	 * @return String
	 * @throws
	 */
	private static String getFileNameFromPath(String path) {
		int location = path.lastIndexOf("/");
		if (location == -1) {
			return null;
		}
		String name = path.substring(location + 1);
		return name;
	}
}
