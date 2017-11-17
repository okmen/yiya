package com.bbyiya.pic.service.calendar;

import com.bbyiya.pic.vo.calendar.GroupActivityAddParam;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_GroupActivityService {
	/**
	 * 添加分销活动
	 * @param userid
	 * @param param
	 * @return
	 */
	ReturnModel addorEditGroupActivity(Long userid, GroupActivityAddParam param);

	/**
	 * 分销列表
	 * @param index
	 * @param size
	 * @param userid
	 * @param status
	 * @param keywords
	 * @return
	 */
	ReturnModel findGroupActivityList(int index, int size, Long userid,
			Integer status, String keywords);
	/**
	 *  活动制作进度列表
	 * @param index
	 * @param size
	 * @param gactid
	 * @param addresstype
	 * @param keywords
	 * @return
	 */
	ReturnModel getGroupActWorkListByGactid(int index, int size,
			Integer gactid, Integer addresstype, String keywords);
	
}
