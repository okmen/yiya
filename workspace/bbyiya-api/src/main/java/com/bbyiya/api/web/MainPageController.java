package com.bbyiya.api.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.service.IMusicStoreService;
import com.bbyiya.service.IReadsMgtService;
import com.bbyiya.service.IYiyaTalkService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.music.MusicResult;
import com.bbyiya.vo.reads.ReadsResult;
import com.bbyiya.vo.talks.YiyaTalkBannerModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

/**
 * app主页信息
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/index")
public class MainPageController extends SSOController {

	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "musicStoreService")
	private IMusicStoreService musicService;
	
	
	@Resource(name = "yiyaTalkService")
	private IYiyaTalkService talkService;
	
	@Resource(name = "readsMgtService")
	private IReadsMgtService readService;
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
			List<MusicResult> musiclist=musicService.find_dailyMusiclist(user);
			mapResult.put("dailyMusics",musiclist); // 静态文件 ConfigUtil.getMaplist("muscis")
			//咿呀说
			List<YiyaTalkBannerModel> talks=talkService.find_talkBanners();
			if(talks!=null&&talks.size()>0){
				mapResult.put("yiyatalks", talks) ;
			}else { 
				mapResult.put("yiyatalks", ConfigUtil.getMaplist("yiyaspeaks")) ;
			}
			//每日读物
			List<ReadsResult> dailyReads=readService.find_DailyReadResultlist(user);
			if(dailyReads!=null&&dailyReads.size()>0){
				mapResult.put("dailyReads", dailyReads) ;
			}else {
				mapResult.put("dailyReads", ConfigUtil.getMaplist("reads")) ;
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(mapResult); 
			
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登陆过期，请重新登陆！");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	

}
