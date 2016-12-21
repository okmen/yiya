package com.bbyiya.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.SDailyreadsMapper;
import com.bbyiya.model.SDailyreads;
import com.bbyiya.service.IReadsMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.vo.reads.DailyReadResult;
import com.bbyiya.vo.user.LoginSuccessResult;

@Service("readsMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class ReadsMgtServiceImpl implements IReadsMgtService {
	@Autowired
	private SDailyreadsMapper dailyreadsMapper;

	public List<DailyReadResult> find_DailyReadResultlist(LoginSuccessResult user) {
		if (user != null && user.getBabyInfo() != null) {
			try {
				int days = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
				List<SDailyreads> list = dailyreadsMapper.findDailyReadslist(days);
				if(list!=null&&list.size()>0){
					List<DailyReadResult> results=new ArrayList<DailyReadResult>();
					for (SDailyreads daliy : list) {
						DailyReadResult mo=new DailyReadResult();
						mo.setTitle(daliy.getTitle());
						mo.setContent(daliy.getContent());
						mo.setSummary(daliy.getSummary());
						mo.setSourceUrl(daliy.getSourceurl());
						results.add(mo);
					}
					return results;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}
