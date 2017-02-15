package com.bbyiya.pic.web.common;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.UBranchinfotemp;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.utils.ObjectUtil;

public class Json2Objects {

	/**
	 * 001
	 * 供应商报名页申请
	 * @param addrJson
	 * @return
	 */
	public static UBranchinfotemp getParam_UBranchinfotemp(String addrJson) {
		try {
			JSONObject model = JSONObject.fromObject(addrJson);
			if (model != null) {
				UBranchinfotemp param = new UBranchinfotemp();
				String contactname=String.valueOf(model.get("contactname"));
				if(!ObjectUtil.isEmpty(contactname)&&!"null".equals(contactname)){
					param.setContactname(contactname);
				}
				String companyname=String.valueOf(model.get("companyname"));
				if(!ObjectUtil.isEmpty(companyname)&&!"null".equals(companyname)){
					param.setCompanyname(companyname);
				}
				String phone=String.valueOf(model.get("phone"));
				if(!ObjectUtil.isEmpty(phone)&&!"null".equals(phone)){
					param.setPhone(phone);
				}
				Integer province=ObjectUtil.parseInt(String.valueOf(model.get("province")));
				if(province!=null&&province>0){
					param.setProvince(province);
				}
				Integer city=ObjectUtil.parseInt(String.valueOf(model.get("city")));
				if(city!=null&&city>0){
					param.setCity(city);
				}
				return param;
			}
		} catch (Exception e) {
			
		}
		return null;
		
	}
	
	/**
	 * 002
	 * 保存参数对象转化
	 * @param result
	 * @return
	 */
	public static MyProductParam getParam_MyProductParam(String result) {
		JSONObject model = JSONObject.fromObject(result);
		if (model != null) {
			MyProductParam param = new MyProductParam();
			param.setProductid(ObjectUtil.parseLong(String.valueOf(model.get("productid"))));
			param.setTitle(String.valueOf(model.get("title")));
			param.setAuthor(String.valueOf(model.get("author")));
			param.setCartid(ObjectUtil.parseLong(String.valueOf(model.get("cartid"))));
			JSONArray details = new JSONArray().fromObject(String.valueOf(model.get("details")));
			if(details!=null&&details.size()>0){
				List<PMyproductdetails> detailsList=new ArrayList<PMyproductdetails>();
				for (int i = 0; i < details.size(); i++) {
					JSONObject dd = details.getJSONObject(i);//
					PMyproductdetails mo=new PMyproductdetails();
					String content=String.valueOf(dd.get("content"));
					if(!ObjectUtil.isEmpty(content)&&!content.equals("null")){
						mo.setContent(content);
					}
					int scenid=ObjectUtil.parseInt(String.valueOf(dd.get("sceneid")));
					if(scenid>0){
						mo.setSceneid(scenid); 
					}
					if(dd.get("imgurl")!=null){
						String url=String.valueOf(dd.get("imgurl"));
						if(!ObjectUtil.isEmpty(url)&&!url.equals("null")){
							mo.setImgurl(url); 
						}	
					}
					int sort=ObjectUtil.parseInt(String.valueOf(dd.get("sort")));
					if(sort>0){
						mo.setSort(sort); 
					}
					long pdid=ObjectUtil.parseLong(String.valueOf(dd.get("pdid")));
					if(pdid>0){
						mo.setPdid(pdid); 
					}
					detailsList.add(mo);
				}
				param.setDetails(detailsList); 
			}
			return param;
		}
		return null;
		
	}
	
	public static UUseraddress getParam_UUseraddress(String addrJson) {
		JSONObject model = JSONObject.fromObject(addrJson);
		if (model != null) {
			UUseraddress param = new UUseraddress();
			Long addrId=ObjectUtil.parseLong(String.valueOf(model.get("addrid")));
			if(addrId!=null&&addrId>0){
				param.setAddrid(addrId); 
			}
			String reciver=String.valueOf(model.get("reciver"));
			if(!ObjectUtil.isEmpty(reciver)&&!"null".equals(reciver)){
				param.setReciver(reciver);
			}
			String phone=String.valueOf(model.get("phone"));
			if(!ObjectUtil.isEmpty(phone)&&!"null".equals(phone)){
				param.setPhone(phone);
			}
			Integer province=ObjectUtil.parseInt(String.valueOf(model.get("province")));
			if(province!=null&&province>0){
				param.setProvince(province);
			}
			Integer city=ObjectUtil.parseInt(String.valueOf(model.get("city")));
			if(city!=null&&city>0){
				param.setCity(city);
			}
			Integer area=ObjectUtil.parseInt(String.valueOf(model.get("area")));
			if(area!=null&&area>0){
				param.setArea(area);
			}
			String streetdetail=String.valueOf(model.get("streetdetail"));
			if(!ObjectUtil.isEmpty(streetdetail)&&!"null".equals(streetdetail)){
				param.setStreetdetail(streetdetail);
			}
			return param;
		}
		return null;
		
	}
}
