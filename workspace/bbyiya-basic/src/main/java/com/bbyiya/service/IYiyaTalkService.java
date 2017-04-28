package com.bbyiya.service;

import java.util.List;

import com.bbyiya.model.MYiyabanner;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.talks.YiyaTalkBannerModel;

public interface IYiyaTalkService {

	/**
	 * 获取app首页咿呀说 banner
	 * 
	 * @return
	 */
	List<YiyaTalkBannerModel> find_talkBanners();

	/**
	 * 新增/修改 咿呀说 banner
	 * 
	 * @param adminId
	 * @param param
	 * @return
	 */
	ReturnModel addOrEdit_yiyaTalkBanner(Integer adminId, MYiyabanner param);
}
