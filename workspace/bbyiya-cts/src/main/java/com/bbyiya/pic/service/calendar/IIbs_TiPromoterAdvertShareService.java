package com.bbyiya.pic.service.calendar;

import java.util.List;

import com.bbyiya.model.TiPromoteradvertimgs;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_TiPromoterAdvertShareService {
	/**
	 * 分享广告设置
	 * @param promoterUserId
	 * @param advertinfo
	 * @param advertimgs
	 * @return
	 */
	ReturnModel addOrEditShareAdvert(Long promoterUserId,
			TiPromoteradvertinfo advertinfo,
			List<TiPromoteradvertimgs> advertimgs);
	/**
	 *  清除设置
	 * @param advertid
	 * @return
	 */
	ReturnModel resetAdvertInfo(Long promoterUserId);
	/**
	 * 加载推广商分享广告信息
	 * @param promoterUserId
	 * @return
	 */
	ReturnModel getPromoterShareAdvert(Long promoterUserId);
	/**
	 * 推广商分享广告报名客户列表
	 * @param promoterUserId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel getPromoteradvertCoustomer(Long promoterUserId, int index,
			int size);
	
	
	
}
