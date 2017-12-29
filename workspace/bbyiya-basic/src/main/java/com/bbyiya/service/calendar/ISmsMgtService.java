package com.bbyiya.service.calendar;

public interface ISmsMgtService {
	/**
	 * 完成点赞，短信通知
	 * @param workId
	 * @return
	 */
	boolean sendMsg_ActivityCompleteShare(long workId);

}
