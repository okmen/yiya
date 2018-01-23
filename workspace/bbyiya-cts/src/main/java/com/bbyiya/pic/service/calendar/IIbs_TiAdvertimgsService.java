package com.bbyiya.pic.service.calendar;

import com.bbyiya.vo.ReturnModel;

public interface IIbs_TiAdvertimgsService {
	
	/**
	 * 添加推广商广告位。
	 * @param promoterUserId
	 * @param productid
	 * @param advertimgjson
	 * @return
	 */
	ReturnModel addOrEditAdvertimgs(Long promoterUserId, Long productid,
			String advertimgjson,String content);
	
	/**
	 * 得到产品的广告位信息
	 * @param promoterUserId
	 * @param productid
	 * @return
	 */
	ReturnModel getAdvertimgsByIds(Long promoterUserId, Long productid);

	
	
}
