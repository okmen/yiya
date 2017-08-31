package com.bbyiya.pic.service.calendar;
import java.util.List;

import com.bbyiya.pic.vo.calendar.CalendarActivityAddParam;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_CalendarActivityService {
	/**
	 * 添加日历活动信息
	 * @param userid
	 * @param title
	 * @param remark
	 * @return
	 */
	ReturnModel addCalendarActivity(Long userid,CalendarActivityAddParam param);
	/**
	 * 修改日历活动信息
	 * @param param
	 * @return
	 */
	ReturnModel editCalendarActivity(CalendarActivityAddParam param);
	/**
	 * 修改活动备注
	 * @param actid
	 * @param remark
	 * @return
	 */
	ReturnModel editActivityRemark(Integer actid, String remark);
	/**
	 * 活动列表
	 * @param index
	 * @param size
	 * @param userid
	 * @param status
	 * @param keywords
	 * @param type
	 * @return
	 */
	ReturnModel findCalendarActivityList(int index, int size, Long userid,
			Integer status, String keywords, Integer type);
	/**
	 * 影楼员工无权限活动信息列表
	 * @param index
	 * @param size
	 * @param promoterUserId
	 * @param actid
	 * @return
	 */
	ReturnModel findActivityoffList(int index, int size, Long promoterUserId,
			Integer actid);
	/**
	 * 设置员工模板负责权限
	 * @param userId
	 * @param actid
	 * @param status
	 * @return
	 */
	ReturnModel setUserActPromotePermission(Long userId, Integer actid,
			Integer status);
	
	
}
