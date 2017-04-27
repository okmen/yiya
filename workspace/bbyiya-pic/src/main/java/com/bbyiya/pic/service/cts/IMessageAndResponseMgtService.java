package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.ReturnModel;
public interface IMessageAndResponseMgtService {
	
	/**
	 * 添加系统通知
	 * @param title
	 * @param content
	 * @return
	 */
	ReturnModel addSysMessage(String title, String content);
	/**
	 * 获取意见反馈列表
	 * @param index
	 * @param size
	 * @param startTimeStr
	 * @param endTimeStr
	 * @return
	 */
	ReturnModel getUserResponseList(int index, int size, String startTimeStr,
			String endTimeStr);

	
	
}
