package com.bbyiya.utils.pay;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class ParamUtils {

	
	
	public static SortedMap<String, String> xml2Map(String xml) {
		try {
			SortedMap<String, String> sortedMap= new TreeMap<String, String>();
			Document document = DocumentHelper.parseText(xml);
			Element root=	document.getRootElement();
			
			List<Element> list=root.elements();
			if(list!=null&&list.size()>0)
			{
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
}
