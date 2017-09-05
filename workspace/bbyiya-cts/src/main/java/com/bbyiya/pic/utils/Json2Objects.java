package com.bbyiya.pic.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bbyiya.model.PMyproducttempext;
import com.bbyiya.model.TiMachinemodel;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.RAreaVo;

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
	
	
	public static List<TiMachinemodel> getParam_Machinemodel(String machineJson) {
		if(ObjectUtil.isEmpty(machineJson)){
			return null;
		}
		JSONObject model = JSONObject.fromObject(machineJson);
		List<TiMachinemodel> machinelist=null;
		if (model != null) {
			String detailString=String.valueOf(model.get("machineList"));
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
				machinelist=new ArrayList<TiMachinemodel>();
				for (int i = 0; i < codearr.size(); i++) {
					JSONObject dd = codearr.getJSONObject(i);
					TiMachinemodel machine=new TiMachinemodel();
					int machineid=ObjectUtil.parseInt(String.valueOf(dd.get("machineid")));
					if(machineid>0){
						machine.setMachineid(machineid);
					}
					String name=String.valueOf(dd.get("name"));
					
					if (!(ObjectUtil.isEmpty(name) || "null".equals(name))) {
						machine.setName(name);
					}
					if(machine.getMachineid()!=null&&machine.getMachineid()!=null){
						machinelist.add(machine);
					}
					
				}
			} 
		}
		
		return machinelist;
	}
	
	public static List<RAreaVo> getParam_RAreaVo(String areacodeJson) {
		if(ObjectUtil.isEmpty(areacodeJson)){
			return null;
		}
		JSONObject model = JSONObject.fromObject(areacodeJson);
		List<RAreaVo> arealist=null;
		if (model != null) {
			String detailString=String.valueOf(model.get("areacodelist"));
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
				arealist=new ArrayList<RAreaVo>();
				for (int i = 0; i < codearr.size(); i++) {
					JSONObject dd = codearr.getJSONObject(i);
					RAreaVo applyarea=new RAreaVo();
					int areacode=ObjectUtil.parseInt(String.valueOf(dd.get("areacode")));
					if(areacode>0){
						applyarea.setAreacode(areacode);
					}
					int provincecode=ObjectUtil.parseInt(String.valueOf(dd.get("provincecode")));
					if(provincecode>0){
						applyarea.setProvincecode(provincecode);
					}
					int citycode=ObjectUtil.parseInt(String.valueOf(dd.get("citycode")));
					if(citycode>0){
						applyarea.setCitycode(citycode);
					}
					if(applyarea.getAreacode()!=null&&applyarea.getProvincecode()!=null&&applyarea.getCitycode()!=null){
						arealist.add(applyarea);
					}
					
				}
			} 
			
		}
		
		return arealist;
	}
	
}
