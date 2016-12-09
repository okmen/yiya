package com.bbyiya.api.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.SMusicrecommend;
import com.bbyiya.service.IMusicStoreService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/index")
public class MainPageController extends SSOController {

	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "musicStoreService")
	private IMusicStoreService musicService;
	/**
	 * M01主页
	 * @return  a咿呀说，b今日音乐
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/mainInfo")
	public String mainInfo() throws Exception {
		LoginSuccessResult user=super.getLoginUser();
		ReturnModel rq=new ReturnModel() ;
		if (user != null) {
			Map<String, Object> mapResult=new HashMap<String, Object>();
			//每日推荐音乐
			List<SMusicrecommend> musiclist=musicService.find_SMusicrecommend(user.getUserId());
			mapResult.put("musiclist", musiclist);
			
			//咿呀说
			mapResult.put("talks", ConfigUtil.getMaplist("yiyaspeaks")) ;
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(mapResult); 
			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登陆过期，请重新登陆！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
