package com.bbyiya.pic.service.cts;

import java.text.ParseException;

import com.bbyiya.vo.ReturnModel;


public interface IActivityStatisticsService {
	
	/**
	 * 活动统计页面
	 * @param agentuserid
	 * @return
	 */
	ReturnModel getActivityStaticsticsPage(Long agentuserid,int index,int size);
	
	/**
	 * 单个活动的统计页
	 * @param tempid
	 * @param starttime
	 * @param endtime
	 * @param type
	 * @return
	 */
	ReturnModel activityDetailsCountPage(Integer tempid, String starttime,
			String endtime, Integer type)throws ParseException;
	
	/**
	 * 得到代理商列表
	 * @return
	 * @throws ParseException
	 */
	ReturnModel getAgentList();
	
	/**
	 * cts代理商订单统计总览
	 * @param agentuserid
	 * @return
	 */
	ReturnModel OrderCountPage(Long agentuserid);
	
	
}