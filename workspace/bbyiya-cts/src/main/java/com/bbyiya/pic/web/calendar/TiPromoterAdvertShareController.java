package com.bbyiya.pic.web.calendar;



import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.TiPromoteradvertimgs;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.pic.service.calendar.IIbs_TiPromoterAdvertShareService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_advertshare")
public class TiPromoterAdvertShareController extends SSOController {
	
	@Resource(name = "ibs_TiPromoterAdvertShareService")
	private IIbs_TiPromoterAdvertShareService advertshareService;
	@Autowired
	private UUsersMapper userMapper;
	
	/**
	 * 添加或编辑影楼分享广告
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrEditShareAdvert")
	public String addOrEditShareAdvert(String advertinfoJson,String advertImgsJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiPromoteradvertinfo advertinfo=(TiPromoteradvertinfo) JsonUtil.jsonStrToObject(advertinfoJson, TiPromoteradvertinfo.class);
			List<ImageInfo> imageinfolist = Json2Objects.getParam_ImageInfo(advertImgsJson);
			List<TiPromoteradvertimgs> advertimgslist=null;
			if(imageinfolist!=null&&imageinfolist.size()>0){
				advertimgslist=new ArrayList<TiPromoteradvertimgs>();
				for (ImageInfo img : imageinfolist) {
					TiPromoteradvertimgs advertimg=new TiPromoteradvertimgs();
					advertimg.setImgurl(img.getUrl());
					advertimgslist.add(advertimg);
				}
			}	
			rq=advertshareService.addOrEditShareAdvert(user.getUserId(), advertinfo, advertimgslist);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 设置默认或取消默认广告
	 * @param advertinfoJson
	 * @param advertImgsJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setDefaultAdvert")
	public String setDefaultAdvert(Integer advertid,Integer isdefault) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=advertshareService.setDefaultAdvert(user.getUserId(), advertid,isdefault);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 得到全局分享广告信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPromoterShareAdvert")
	public String getPromoterShareAdvert() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=advertshareService.getPromoterShareAdvert(user.getUserId());
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 活动分享广告详情信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getPromoterShareAdvertById")
	public String getPromoterShareAdvertById(Integer advertid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=advertshareService.getPromoterShareAdvertById(user.getUserId(),advertid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 清除分享全局广告信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/resetAdvertInfo")
	public String resetAdvertInfo() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=advertshareService.resetAdvertInfo(user.getUserId());
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 删除分享广告信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteAdvertInfo")
	public String deleteAdvertInfo(Integer advertid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=advertshareService.deleteAdvertInfo(user.getUserId(),advertid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 得到广告配置列表
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getShareAdvertList")
	public String getShareAdvertList(String keywords,Integer index,Integer size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(index==null) index=1;
			if(size==null) size=20;
			rq=advertshareService.getShareAdvertList(user.getUserId(),keywords,index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 得到客户报名信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getcoustomerlist")
	public String getPromoteradvertCoustomer(Integer advertid,Integer index,Integer size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(index==null) index=1;
			if(size==null) size=10;
			rq=advertshareService.getPromoteradvertCoustomer(user.getUserId(),advertid,index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 设置活动分享广告
	 * @param advertinfoJson
	 * @param advertImgsJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setActsShareAdvert")
	public String setActsShareAdvert(Integer actid,Integer advertid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){

			rq=advertshareService.setActsShareAdvert(user.getUserId(), actid,advertid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
