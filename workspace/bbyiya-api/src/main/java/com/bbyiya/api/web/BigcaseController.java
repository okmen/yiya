package com.bbyiya.api.web;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IBigCaseService;
import com.bbyiya.service.impl.BigCaseServiceImpl;
import com.bbyiya.service.impl.BigCaseServiceImpl.BigCaseTime;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.bigcase.BigcaseResult;
import com.bbyiya.vo.bigcase.BigcaseStageResult;
import com.bbyiya.vo.bigcase.BigcasesummaryResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/index")
public class BigcaseController extends SSOController {
	@Resource(name = "bigCaseService")
	private IBigCaseService bigcaseService;

	/**
	 * M02 主页-宝宝大事件
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/bigCaselist")
	public String bigCaselist(@RequestParam(required = false, defaultValue = "1") int index, @RequestParam(required = false, defaultValue = "4") int size) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
//			int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
//			BigCaseTime timeParam = new BigCaseServiceImpl().getStartAndEndDay(day);
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("timeId", timeParam.getTimeId());
//			result.put("timeslist", ConfigUtil.getMaplist("timeIntervals"));
			/**
			 * 大事件列表
			 */
//			PageInfo<BigcaseResult> resultInfo = bigcaseService.find_MBigcaseResultPage(user, index, size);
//			result.put("bigcase", resultInfo);
//			rqModel.setStatu(ReturnStatus.Success);
//			rqModel.setBasemodle(result);
			rqModel=bigcaseService.find_MBigcaseResultIndexPage(user);
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	/**
	 * M07 大事件-阶段列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getTimelist")
	public String getTimelist() throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
			BigCaseTime timeParam = new BigCaseServiceImpl().getStartAndEndDay(day);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("timeId", timeParam.getTimeId());
			result.put("timeslist", ConfigUtil.getMaplist("timeIntervals"));
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(result);
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	/**
	 * M08 大事件-阶段总结
	 * @param timeId 阶段ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getStageInfo")
	public String getStageInfo(@RequestParam(required = false, defaultValue = "0") int timeId ) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			List<BigcasesummaryResult> list=bigcaseService.getBigcasesummaryResult(timeId);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("list", list);
			List<Map<String, String>> mapList= ConfigUtil.getMaplist("timeIntervals");
			map.put("title", "咿呀事件将培养神奇宝宝在时间上划分为"+mapList.size()+"个阶段，这是第"+timeId+"个阶段");
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(map);
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	
	/**
	 * M03 大事件详情
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/eventDetail")
	public String eventDetail(@RequestParam(required = false, defaultValue = "0") int caseId ) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null&&user.getUserId()!=null) {
			rqModel=bigcaseService.getBigcaseDetails(user, caseId);
		}
		return JsonUtil.objectToJsonStr(rqModel);
	} 
	
	/**
	 * M05 我的收藏-添加收藏
	 * @param caseId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addCollect")
	public String addCollect(@RequestParam(required = false, defaultValue = "0") int caseId ) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null&&user.getUserId()!=null) {
			rqModel=bigcaseService.addCollection(user.getUserId(), caseId);
		}else {
			rqModel.setStatu(ReturnStatus.LoginError);
		} 
		return JsonUtil.objectToJsonStr(rqModel);
	} 
	
	/**
	 * M06 我的收藏-取消收藏
	 * @param caseId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delCollect")
	public String delCollect(@RequestParam(required = false, defaultValue = "0") int caseId ) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null&&user.getUserId()!=null) {
			rqModel=bigcaseService.deleCollection(user.getUserId(), caseId);
		}else {
			rqModel.setStatu(ReturnStatus.LoginError);
		} 
		return JsonUtil.objectToJsonStr(rqModel);
	} 
	
	/**
	 * M04 我的收藏-大事件列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCollectlist")
	public String getCollectlist() throws Exception {
		ReturnModel rqModel = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null&&user.getUserId()!=null) {
			List<BigcaseResult> list=bigcaseService.find_MBigcasecollectlist(user.getUserId());
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(list); 
		}else {
			rqModel.setStatu(ReturnStatus.LoginError);
		} 
		return JsonUtil.objectToJsonStr(rqModel);
	} 
	
	
	
}
