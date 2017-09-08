package com.bbyiya.pic.web.calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.calendar.IIbs_CalendarActivityService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.pic.vo.calendar.CalendarActivityAddParam;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/calendar/act/")
public class CalendarActivityController extends SSOController {
	
	@Resource(name = "ibs_CalendarActivityService")
	private IIbs_CalendarActivityService calendarActivityService;
	
	
	
	/**
	 * 创建日历活动
	 * @param myproductTempJson
	 * @param productstyleJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addCalendarActivity")
	public String addCalendarActivity(String activityJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			CalendarActivityAddParam param = (CalendarActivityAddParam)JsonUtil.jsonStrToObject(activityJson,CalendarActivityAddParam.class);
			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getProductid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请选择活动奖品!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getFreecount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getExtCount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("完成条件的分享数不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getFreecount()!=null&&param.getFreecount()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能小于0!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getExtCount()!=null&&param.getExtCount()<=5){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("完成条件的分享数必须大于等于5个!");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDescription())&&!ObjectUtil.validSqlStr(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
					
			rq=calendarActivityService.addCalendarActivity(user.getUserId(),param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/editCalendarActivity")
	public String editCalendarActivity(String activityJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			CalendarActivityAddParam param = (CalendarActivityAddParam)JsonUtil.jsonStrToObject(activityJson,CalendarActivityAddParam.class);
			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getFreecount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getFreecount()!=null&&param.getFreecount()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能小于零!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDescription())&&!ObjectUtil.validSqlStr(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			rq=calendarActivityService.editCalendarActivity(param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 合成图片调的接口
	 * @param actid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/composeActImg")
	public String composeActImg(Integer actid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if (actid==null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("actid不能为空");
				return JsonUtil.objectToJsonStr(rq);
			}	
			rq=calendarActivityService.composeActImg(user.getUserId(), actid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/savecomposeActImg")
	public String savecomposeActImg(Integer actid,String actimg) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if (actid==null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("actid不能为空");
				return JsonUtil.objectToJsonStr(rq);
			}	
			rq=calendarActivityService.savecomposeActImg(actid,actimg);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * ibs推广者得到活动列表
	 * @param index
	 * @param size
	 * @param status
	 * @param keywords
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCalendarActivityList")
	public String getCalendarActivityList(int index,int size,Integer status,String keywords,Integer type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=calendarActivityService.findCalendarActivityList(index, size, user.getUserId(),status,keywords,type);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 得到活动制作进度情况列表
	 * @param index
	 * @param size
	 * @param status
	 * @param keywords
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getActWorkListByActId")
	public String getActWorkListByActId(int index,int size,Integer actid,Integer status,String keywords) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=calendarActivityService.getActWorkListByActId(index, size,actid,status,keywords);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 编辑活动备注信息
	 * @param tempid
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editActRemark")
	public String editActRemark(Integer actid,String remark) throws Exception {
		ReturnModel rq=new ReturnModel();
		rq=calendarActivityService.editActivityRemark(actid, remark);
		return JsonUtil.objectToJsonStr(rq);
		
	}
	
	
	/**
	 * 员工活动权限列表
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findActivityoffList")
	public String findActivityoffList(Integer index,Integer size,Integer actid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=calendarActivityService.findActivityoffList(index, size, user.getUserId(), actid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 设置员工负责活动推广权限
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setUserActPermission")
	public String setUserActPromotePermission(Long userId,Integer actid,Integer status) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(status==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("status参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=calendarActivityService.setUserActPromotePermission(userId, actid, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
