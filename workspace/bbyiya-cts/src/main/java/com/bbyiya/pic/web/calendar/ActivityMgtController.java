package com.bbyiya.pic.web.calendar;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.calendar.ItiAcitivityMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/calendar/act/")
public class ActivityMgtController  extends SSOController {
	
	@Resource(name = "ti_AcitivityMgtServiceImpl")
	private ItiAcitivityMgtService activityService;
	
	/**
	 * 使参加活动的作品，名额失效
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/failActivityWork")
	public String addCalendarActivity(long  workId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=activityService.updateActivityWorkTofailse(user.getUserId(), workId);
		}else{
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 激活
	 * @param workId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/invokeActivityWorkStatus")
	public String invokeActivityWorkStatus(long  workId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=activityService.invokeActivityWorkStatus(user.getUserId(), workId);
		}else{
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
