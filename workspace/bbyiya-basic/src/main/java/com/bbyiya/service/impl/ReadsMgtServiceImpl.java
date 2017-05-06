package com.bbyiya.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.SDailyreadsMapper;
import com.bbyiya.dao.SReadsMapper;
import com.bbyiya.dao.SReadstypesMapper;
import com.bbyiya.model.SDailyreads;
import com.bbyiya.model.SReads;
import com.bbyiya.model.SReadstypes;
import com.bbyiya.service.IReadsMgtService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.vo.reads.ReadsResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("readsMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class ReadsMgtServiceImpl implements IReadsMgtService {
	@Autowired
	private SDailyreadsMapper dailyreadsMapper;
	@Autowired
	private SReadstypesMapper readstypesMapper;
	@Autowired
	private SReadsMapper readsMapper;
	
	public List<ReadsResult> find_DailyReadResultlist(LoginSuccessResult user) {
		if (user != null && user.getBabyInfo() != null) {
			try {
				int days = DateUtil.daysBetween(user.getBabyInfo().getBirthday(), new Date());
				List<SDailyreads> list = dailyreadsMapper.findDailyReadslist(days);
				if(list!=null&&list.size()>0){
					List<ReadsResult> results=new ArrayList<ReadsResult>();
					for (SDailyreads daliy : list) {
						ReadsResult mo=new ReadsResult();
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
	
	public List<SReadstypes> find_SReadstypeslist(){
		return readstypesMapper.findReadsTypelist();
	}
	
	public PageInfo<ReadsResult> find_SReadsPageInfo(int readType,int index,int size){
		PageHelper.startPage(index, size);
		List<ReadsResult> list=readsMapper.findSReadsByTypeId(readType);
		return new PageInfo<ReadsResult>(list);
	}
}
