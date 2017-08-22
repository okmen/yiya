package com.bbyiya.pic.web.cts;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.cts.IActivityStatisticsService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

/**
 * 活动统计
 * @author kevin
 *
 */
@Controller
@RequestMapping(value = "/cts/statistics")
public class ActivityStatisticsController  extends SSOController{

	@Resource(name = "ctsActivityStatisticsService")
	private IActivityStatisticsService statisticsService;
	
	
	
	@ResponseBody
	@RequestMapping(value = "/activityCountPage")
	public String activityCountPage(Long agentUserId,int index,int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=statisticsService.getActivityStaticsticsPage(agentUserId,index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 
	 * @param tempid 活动ID
	 * @param startDate 开始时间
	 * @param endDate   结束时间
	 * @param type		类型 0 天，1小时
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/activityDetailsCountPage")
	public String activityDetailsCountPage(Integer tempid,String starttime,String endtime,Integer type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=statisticsService.activityDetailsCountPage(tempid,starttime,endtime,type);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
