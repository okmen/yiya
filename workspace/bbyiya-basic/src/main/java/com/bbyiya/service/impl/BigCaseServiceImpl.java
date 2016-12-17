package com.bbyiya.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.model.MBigcase;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.service.IBigCaseService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.github.pagehelper.Page;

@Service("bigCaseService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BigCaseServiceImpl implements IBigCaseService {
	@Autowired
	private UChildreninfoMapper childMapper;

	
	/**
	 * 大事件列表
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	//TODO
	public Page<MBigcase> find_MBigcaseResult(Long userId, int pageIndex, int pageSize) {
		UChildreninfo child = childMapper.selectByPrimaryKey(userId);
		if (child != null) {
			try {
				int day = DateUtil.daysBetween(child.getBirthday(), new Date());
				BigCaseTime timeParam=getStartAndEndDay(day);
				if(timeParam!=null){
					
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 获取宝宝当前属于哪个时间段
	 * @param theday
	 * @return
	 */
	public BigCaseTime getStartAndEndDay(int theday){
		BigCaseTime result=new BigCaseTime();
		List<Map<String, String>> timelist = ConfigUtil.getMaplist("timeIntervals");
		for (Map<String, String> map : timelist) {
			int start= ObjectUtil.parseInt(map.get("start"));
			int end= ObjectUtil.parseInt(map.get("end"));
			if(theday>=start&&theday<=end){//找到这个时期
				result.setStart(start);
				result.setEnd(end);
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @author Administrator
	 *
	 */
	public class BigCaseTime{
		private int start;
		private int end;
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
		
	}
}


