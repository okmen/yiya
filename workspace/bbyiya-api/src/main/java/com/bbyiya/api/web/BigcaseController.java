package com.bbyiya.api.web;

import java.util.Date;
import java.util.HashMap;
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
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/index")
public class BigcaseController extends SSOController {
	@Resource(name = "bigCaseService")
	private IBigCaseService bigcaseService;

	/**
	 * 宝宝大事件
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
			int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
			BigCaseTime timeParam = new BigCaseServiceImpl().getStartAndEndDay(day);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("timeId", timeParam.getTimeId());
			result.put("timeslist", ConfigUtil.getMaplist("timeIntervals"));
			/**
			 * 大事件列表
			 */
			PageInfo<BigcaseResult> resultInfo = bigcaseService.find_MBigcaseResultPage(user, index, size);
			result.put("bigcase", resultInfo);
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(result);
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
	/**
	 * 大事件详情
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
}
