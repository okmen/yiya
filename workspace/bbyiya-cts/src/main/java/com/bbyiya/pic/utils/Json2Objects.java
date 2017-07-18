package com.bbyiya.pic.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bbyiya.model.PMyproducttempext;
import com.bbyiya.utils.ObjectUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Json2Objects {

	public static List<PMyproducttempext> getParam_Myproducttempext(String productstyleJson) {
		if(ObjectUtil.isEmpty(productstyleJson)){
			return null;
		}
		JSONObject model = JSONObject.fromObject(productstyleJson);
		List<PMyproducttempext> stylelist=null;
		if (model != null) {
			String detailString=String.valueOf(model.get("stylelist"));
			if(ObjectUtil.isEmpty(detailString)||detailString.equals("null")){
				return null;
			}	
			JSONArray codearr=null;
			try {
				codearr = new JSONArray().fromObject(detailString);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(codearr!=null&&codearr.size()>0){
				stylelist=new ArrayList<PMyproducttempext>();
				HashMap<Long, Long> map=new HashMap<Long, Long>();
				for (int i = 0; i < codearr.size(); i++) {
					JSONObject dd = codearr.getJSONObject(i);
					PMyproducttempext tempext=new PMyproducttempext();
					Long productId=ObjectUtil.parseLong(String.valueOf(dd.get("productId")));
					if(productId>0){
						tempext.setProductid(productId);
					}
					Long styleId=ObjectUtil.parseLong(String.valueOf(dd.get("styleId")));
					if(styleId>0){
						tempext.setStyleid(styleId);
					}
					//去重
					if(!map.containsKey(styleId)){
						stylelist.add(tempext);
						map.put(styleId, productId);
					}
					
				}
			}
			
		}
		return stylelist;
	}
	
}
