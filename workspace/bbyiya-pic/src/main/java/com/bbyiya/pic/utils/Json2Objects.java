package com.bbyiya.pic.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.bbyiya.model.PCommentstemp;
import com.bbyiya.model.PCommentstips;
import com.bbyiya.model.PMyproductcomments;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranchinfotemp;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.model.UWeiuserapplys;
import com.bbyiya.pic.vo.LoginTempVo;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.user.UChildInfoParam;

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
				Integer district=ObjectUtil.parseInt(String.valueOf(model.get("district")));
				if(district!=null&&district>0){
					param.setDistrict(district);
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
			param.setCartid(ObjectUtil.parseLong(String.valueOf(model.get("cartid"))));
			param.setProductid(ObjectUtil.parseLong(String.valueOf(model.get("productid"))));
			String title=String.valueOf(model.get("title"));
			if(!ObjectUtil.isEmpty(title)&&!"null".equals(title)){
				param.setTitle(title);
			}
			String author=String.valueOf(model.get("author"));
			if(!ObjectUtil.isEmpty(author)&&!"null".equals(author)){
				param.setAuthor(author);
			}
			String desc=String.valueOf(model.get("description"));
			if(!ObjectUtil.isEmpty(desc)&&!"null".equals(desc)){
				param.setDescription(desc);
			}
			String childJsonString=String.valueOf(model.get("childInfo"));
			if(!ObjectUtil.isEmpty(childJsonString)&&!"null".equals(childJsonString)){
				JSONObject chidMod = JSONObject.fromObject(childJsonString);
				if(chidMod!=null){
					boolean isEdit=false;
					UChildInfoParam childParam=new UChildInfoParam();
					String nickname=String.valueOf(chidMod.get("nickName"));
					if(!ObjectUtil.isEmpty(nickname)&&!"null".equals(nickname)){
						childParam.setNickName(nickname);
						isEdit=true;
					}
					String birthday=String.valueOf(chidMod.get("birthday"));
					if(!ObjectUtil.isEmpty(birthday)&&!"null".equals(birthday)){
						childParam.setBirthday(birthday);
						isEdit=true;
					}
					if(isEdit){
						param.setChildInfo(childParam); 
					}
				}
			}
			String detailString=String.valueOf(model.get("details"));
			if(ObjectUtil.isEmpty(detailString)||detailString.equals("null"))
				return param;
			JSONArray details=null;
			try {
				 details = new JSONArray().fromObject(detailString);
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(details!=null&&details.size()>0){
				List<PMyproductdetails> detailsList=new ArrayList<PMyproductdetails>();
				for (int i = 0; i < details.size(); i++) {
					JSONObject dd = details.getJSONObject(i);//
					PMyproductdetails mo=new PMyproductdetails();
					String content=String.valueOf(dd.get("content"));
					if(!ObjectUtil.isEmpty(content)&&!content.equals("null")){
						mo.setContent(content);
					}
					String scenTitle=String.valueOf(dd.get("title"));
					int scenid=ObjectUtil.parseInt(String.valueOf(dd.get("sceneid")));
					if(scenid>0){
						mo.setSceneid(scenid); 
					}
					if (!ObjectUtil.isEmpty(scenTitle)&&!scenTitle.equals("null")) {
						mo.setTitle(scenTitle);
						if(scenid>=0){
							mo.setSceneid(scenid); 
						}
					} 
					String dec=String.valueOf(dd.get("description"));
					if(!ObjectUtil.isEmpty(dec)&&!dec.equals("null")){
						mo.setDescription(dec);
					}
					if(dd.get("imgurl")!=null){
						String url=String.valueOf(dd.get("imgurl"));
						if(!ObjectUtil.isEmpty(url)&&!url.equals("null")){
							mo.setImgurl(ImgDomainUtil.getImageUrl_Full(url));  
							mo.setCreatetime(new Date()); 
							if(mo.getSceneid()==null){
								mo.setSceneid(0); 
							}
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
	
	public static UBranchusers getParam_UBranchusers(String memberJson) {
		JSONObject model = JSONObject.fromObject(memberJson);
		if (model != null) {
			UBranchusers param = new UBranchusers();
			String name=String.valueOf(model.get("name"));
			if(!ObjectUtil.isEmpty(name)&&!"null".equals(name)){
				param.setName(name);
			}
			String phone=String.valueOf(model.get("phone"));
			if(!ObjectUtil.isEmpty(phone)&&!"null".equals(phone)){
				param.setPhone(phone);
			}
			return param;
		}
		return null;
	}
	
	public static UAgentcustomers getParam_UAgentcustomers(String customerJson) {
		JSONObject model = JSONObject.fromObject(customerJson);
		if (model != null) {
			UAgentcustomers param = new UAgentcustomers();
			long customerid=ObjectUtil.parseLong(String.valueOf(model.get("customerid")));
			if(customerid>0){
				param.setCustomerid(customerid); 
			}
			String name=String.valueOf(model.get("name"));
			if(!ObjectUtil.isEmpty(name)&&!"null".equals(name)){
				param.setName(name);
			}
			String phone=String.valueOf(model.get("phone"));
			if(!ObjectUtil.isEmpty(phone)&&!"null".equals(phone)){
				param.setPhone(phone);
			}
			String remark=String.valueOf(model.get("remark"));
			if(!ObjectUtil.isEmpty(remark)&&!"null".equals(remark)){
				param.setRemark(remark);
			}
			String status=String.valueOf(model.get("status"));
			if(!ObjectUtil.isEmpty(status)&&!"null".equals(status)){
				int statusInt=ObjectUtil.parseInt(status);
				if(statusInt>0){
					param.setStatus(statusInt);
				}
			}
			return param;
		}
		return null;
	}
	
	public static LoginTempVo getParam_LoginTempVo(String json) {
		JSONObject model = JSONObject.fromObject(json);
		if (model != null) {
			LoginTempVo param = new LoginTempVo();
			int loginTo=ObjectUtil.parseInt(String.valueOf(model.get("loginTo")));
			if(loginTo>=0){
				param.setLoginTo(loginTo);
			}
			long upUserId=ObjectUtil.parseLong(String.valueOf(model.get("upUserId")));
			if(upUserId>0){
				param.setUpUserId(upUserId);
			}
			String redirect_url=String.valueOf(model.get("redirect_url"));
			if(!"null".equals(redirect_url)&&!ObjectUtil.isEmpty(redirect_url)){
				param.setRedirect_url(redirect_url); 
			}
			return param;
		}
		return null;
	}
	
	public static PMyproductcomments getParam_PMyproductcomments(String json) {
		JSONObject model = JSONObject.fromObject(json);
		if (model != null) {
			PMyproductcomments param = new PMyproductcomments();
			long cartid=ObjectUtil.parseLong(String.valueOf(model.get("cartid")));
			if(cartid>0){
				param.setCartid(cartid);
			}
			String relation=String.valueOf(model.get("relation"));
			if(!ObjectUtil.isEmpty(relation)&&!"null".equals(relation)){
				param.setRelation(relation);
			}
			String content=String.valueOf(model.get("content"));
			if(!ObjectUtil.isEmpty(content)&&!"null".equals(content)){
				param.setContent(content);
			}
			return param;
		}
		return null;
	}
	
	public static UWeiuserapplys getParam_UWeiuserapplys(String weiUserJson) {
		JSONObject model = JSONObject.fromObject(weiUserJson);
		if (model != null) {
			UWeiuserapplys param = new UWeiuserapplys();
			String name=String.valueOf(model.get("name"));
			if(!ObjectUtil.isEmpty(name)&&!"null".equals(name)){
				param.setName(name);
			}
			String certificateimg=String.valueOf(model.get("certificateimg"));
			if(!ObjectUtil.isEmpty(certificateimg)&&!"null".equals(certificateimg)){
				param.setCertificateimg(certificateimg);
			}
			String remark=String.valueOf(model.get("remark"));
			if(!ObjectUtil.isEmpty(remark)&&!"null".equals(remark)){
				param.setRemark(remark);
			}
			long userId=ObjectUtil.parseLong(String.valueOf(model.get("userId")));
			if(userId>0){
				param.setUserid(userId);
			}
			
			return param;
		}
		return null;
	}
	
	public static PCommentstemp getParam_PCommentstemp(String json) {
		JSONObject model = JSONObject.fromObject(json);
		if (model != null) {
			PCommentstemp param = new PCommentstemp();
			long productId=ObjectUtil.parseLong(String.valueOf(model.get("productid")));
			if(productId>0){
				param.setProductid(productId);
			}
			int tipclassid=ObjectUtil.parseInt(String.valueOf(model.get("tipclassid")));
			if(tipclassid>0){
				param.setTipclassid(tipclassid);
			}
			String tipclassname=String.valueOf(model.get("tipclassname"));
			if (!(ObjectUtil.isEmpty(tipclassname) || "null".equals(tipclassname))) {
				param.setTipclassname(tipclassname); 
			}
			String tipsJson=String.valueOf(model.get("tips"));
			if(ObjectUtil.isEmpty(tipsJson)||"null".equals(tipsJson))
				return param;
			try {
				JSONArray details = new JSONArray().fromObject(tipsJson);
				List<PCommentstips> detailsList=new ArrayList<PCommentstips>();
				for (int i = 0; i < details.size(); i++) {
					JSONObject dd = details.getJSONObject(i);
					if (dd != null) {
						PCommentstips mo = new PCommentstips();
						int tipId = ObjectUtil.parseInt(String.valueOf(dd.get("tipid")));
						if (tipId > 0) {
							mo.setTipid(tipId);
						}
						String content = String.valueOf(dd.get("content"));
						if (!(ObjectUtil.isEmpty(content) || "null".equals(content))) {
							mo.setContent(content);
							detailsList.add(mo);
						}
					}
				}
				param.setTips(detailsList); 
			} catch (Exception e) {
				// TODO: handle exception
			}
			return param;
		}
		return null;
	}
}
