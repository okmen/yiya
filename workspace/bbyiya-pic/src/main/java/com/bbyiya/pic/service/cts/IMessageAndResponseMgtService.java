package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.ReturnModel;
public interface IMessageAndResponseMgtService {
	
	/**
	 * ���ϵͳ֪ͨ
	 * @param title
	 * @param content
	 * @return
	 */
	ReturnModel addSysMessage(String title, String content);
	/**
	 * ��ȡ��������б�
	 * @param index
	 * @param size
	 * @param startTimeStr
	 * @param endTimeStr
	 * @return
	 */
	ReturnModel getUserResponseList(int index, int size, String startTimeStr,
			String endTimeStr);

	
	
}
