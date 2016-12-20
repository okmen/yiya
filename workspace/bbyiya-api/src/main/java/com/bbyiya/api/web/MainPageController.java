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
import com.bbyiya.service.IMusicStoreService;
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

/**
 * app��ҳ��Ϣ
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/index")
public class MainPageController extends SSOController {

	/**
	 * ��½��ע�� service
	 */
	@Resource(name = "musicStoreService")
	private IMusicStoreService musicService;
	
	@Resource(name = "bigCaseService")
	private IBigCaseService bigcaseService;
	
	/**
	 * M01��ҳ
	 * @return  a��ѽ˵��b��������
	 * @throws Exception
	 */ 
	@ResponseBody
	@RequestMapping(value = "/mainInfo")
	public String mainInfo() throws Exception {
		LoginSuccessResult user=super.getLoginUser();
		ReturnModel rq=new ReturnModel() ;
		if (user != null) {
			Map<String, Object> mapResult=new HashMap<String, Object>();
			//ÿ���Ƽ�����
//			List<SMusicrecommend> musiclist=musicService.find_SMusicrecommend(user.getUserId());
			mapResult.put("dailyMusics", ConfigUtil.getMaplist("muscis"));
			//��ѽ˵
			mapResult.put("yiyatalks", ConfigUtil.getMaplist("yiyaspeaks")) ;
			//ÿ�ն���
			mapResult.put("dailyReads", ConfigUtil.getMaplist("reads")) ;
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(mapResult); 
			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��½���ڣ������µ�½��");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �������¼�
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/bigCaselist")
	public String bigCaselist(@RequestParam(required = false, defaultValue = "1") int index, @RequestParam(required = false, defaultValue = "4") int size) throws Exception {
		ReturnModel rqModel=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			int day = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
			BigCaseTime timeParam = new BigCaseServiceImpl().getStartAndEndDay(day);
			Map<String, Object> result=new HashMap<String, Object>();
			result.put("timeId", timeParam.getTimeId());
			result.put("timeslist", ConfigUtil.getMaplist("timeIntervals"));
			/**
			 * ���¼��б�
			 */
			PageInfo<BigcaseResult> resultInfo= bigcaseService.find_MBigcaseResultPage(user, index, size);
			result.put("bigcase", resultInfo);
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setBasemodle(result);
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}

}
