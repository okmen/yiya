package com.bbyiya.pic.web.calendar;


import java.net.URLEncoder;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiGroupactivityproducts;
import com.bbyiya.pic.service.calendar.IIbs_CalendarActivityService;
import com.bbyiya.pic.service.calendar.IIbs_GroupActivityService;
import com.bbyiya.pic.vo.calendar.CalendarActivityAddParam;
import com.bbyiya.pic.vo.calendar.GroupActivityAddParam;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_groupact")
public class GroupActivityController extends SSOController {
	
	@Resource(name = "ibs_GroupActivityService")
	private IIbs_GroupActivityService groupActService;
	@Resource(name = "ibs_CalendarActivityService")
	private IIbs_CalendarActivityService calendarActivityService;
	
	
	
	/**
	 * 创建或修改分销活动
	 * @param myproductTempJson
	 * @param productstyleJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addorEditGroupActivity")
	public String addorEditGroupActivity(String activityJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			GroupActivityAddParam param=(GroupActivityAddParam)JsonUtil.jsonStrToObject(activityJson,GroupActivityAddParam.class);

			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动标题不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动说明不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getCompanyname())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("公司名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getProvince())||ObjectUtil.isEmpty(param.getCity())||ObjectUtil.isEmpty(param.getArea())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请选择省市区!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getReciver())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请填写收货人姓名!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getMobilephone())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请填写收货人电话!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getStreetdetails())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请填写详细地址!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getType()!=null&&param.getType().intValue()==1&&param.getPraisecount().intValue()<5){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("广告模式集赞数量不能小于5!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getProductlist()==null||param.getProductlist().size()<=0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请至少选择一种产品!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			rq=groupActService.addorEditGroupActivity(user.getUserId(),param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	
	@ResponseBody
	@RequestMapping(value = "/getGroupActivityList")
	public String getGroupActivityList(int index,int size,Integer status,String keywords) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=groupActService.findGroupActivityList(index, size, user.getUserId(),status,keywords);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getGroupActivityByGactid")
	public String getGroupActivityByGactid(Integer gactid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(gactid==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=groupActService.getGroupActivityByGactid(user.getUserId(),gactid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 保存模板的二维码图片
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/downWorkForGroupRQcode")
	public String downWorkForCusRQcode(String workId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String redirct_url="groupBuy?groupId="+workId;	
			String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(user.getUserId().toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
			rq=calendarActivityService.saveRQcode(urlstr);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getGroupActWorkListByGactid")
	public String getGroupActivityWorkList(int index,int size,Integer gactid,Integer addresstype,String keywords) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=groupActService.getGroupActWorkListByGactid(index, size, gactid,addresstype,keywords);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getSumPostAgeByGactid")
	public String getSumPostAgeByGactid(Integer gactid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=groupActService.getSumPostAgeByGactid(gactid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
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
	@RequestMapping(value = "/setGActsShareAdvert")
	public String setGActsShareAdvert(Integer gactid,Integer advertid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=groupActService.setActsShareAdvert(user.getUserId(), gactid,advertid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
