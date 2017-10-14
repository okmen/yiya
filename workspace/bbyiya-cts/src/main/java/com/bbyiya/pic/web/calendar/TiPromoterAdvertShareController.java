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
	 * 得到分享广告信息
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
	 * 清除分享广告信息
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
	 * 得到客户报名信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getcoustomerlist")
	public String getPromoteradvertCoustomer(Integer index,Integer size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(index==null) index=1;
			if(size==null) size=10;
			rq=advertshareService.getPromoteradvertCoustomer(user.getUserId(),index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
