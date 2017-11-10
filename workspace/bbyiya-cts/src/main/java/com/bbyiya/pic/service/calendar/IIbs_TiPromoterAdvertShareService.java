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
	 * 加载推广商全局分享广告信息
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
	ReturnModel getPromoteradvertCoustomer(Long promoterUserId,Integer advetid, int index,
			int size);
	/**
	 * 加载推广商活动分享广告信息
	 * @param promoterUserId
	 * @param advertid
	 * @return
	 */
	ReturnModel getPromoterShareAdvertById(Long promoterUserId, Integer advertid);
	/**
	 * 设置默认广告
	 * @param promoterUserId
	 * @param advertid
	 * @return
	 */
	ReturnModel setDefaultAdvert(Long promoterUserId, Integer advertid,Integer isdefault);
	/**
	 * 删除广告记录
	 * @param promoterUserId
	 * @param advertid
	 * @return
	 */
	ReturnModel deleteAdvertInfo(Long promoterUserId, Integer advertid);
	/**
	 * 得到分享广告列表
	 * @param promoterUserId
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel getShareAdvertList(Long promoterUserId, String keywords,int index,int size);
	
	/**
	 * 配置活动分享广告
	 * @param promoterUserId
	 * @param actid
	 * @param advertid
	 * @return
	 */
	ReturnModel setActsShareAdvert(Long promoterUserId, Integer actid,
			Integer advertid);
	
	
	
}
