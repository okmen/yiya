package com.bbyiya.pic.service.calendar;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.bbyiya.pic.vo.calendar.CalendarActivityAddParam;
import com.bbyiya.pic.vo.calendar.WorkForCustomerParam;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressVo;

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
	/**
	 * 活动制作进度列表
	 * @param index
	 * @param size
	 * @param keywords
	 * @param type
	 * @return
	 */
	ReturnModel getActWorkListByActId(int index, int size, Integer actid,
			Integer status, String keywords);
	/**
	 * 合成活动图片
	 * @param userid
	 * @param actid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	ReturnModel composeActImg(Long userid, Integer actid)
			throws UnsupportedEncodingException;
	/**
	 * 保存合成图片
	 * @param actid
	 * @param actimg
	 * @return
	 */
	ReturnModel savecomposeActImg(Integer actid, String actimg);
	
	/**
	 * ibs添加代客制作
	 * @param userid
	 * @param workparam
	 * @param addressparam
	 * @return
	 */
	ReturnModel addWorkForCustomer(Long userid, WorkForCustomerParam workparam,
			OrderaddressVo addressparam);
	/**
	 * 代客制作列表
	 * @param userid
	 * @return
	 */
	ReturnModel workForCustomerList(Long userid,int index,int size,String keywords)throws UnsupportedEncodingException;
	/**
	 * 下载二维码
	 * @param url
	 * @return
	 * @throws Exception
	 */
	ReturnModel saveRQcode(String url) throws Exception;
	/**
	 * 添加代客制作预览
	 * @param userid
	 * @param workparam
	 * @return
	 */
	ReturnModel reviewWorkForCustomer(Long userid,
			WorkForCustomerParam workparam);
	
	
}
