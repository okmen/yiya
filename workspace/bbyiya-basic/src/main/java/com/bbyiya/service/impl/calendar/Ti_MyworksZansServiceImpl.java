package com.bbyiya.service.impl.calendar;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.TiMyworkszanlogsMapper;
import com.bbyiya.model.TiMyworkszanlogs;
import com.bbyiya.service.calendar.ITi_MyworksZansService;
import com.bbyiya.vo.user.LoginSuccessResult;

@Service("ti_myworksZansServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ti_MyworksZansServiceImpl implements ITi_MyworksZansService{
	@Autowired
	private TiMyworkszanlogsMapper zanMapper;
	
	/**
	 * 新增点赞记录
	 */
	public boolean addZan(LoginSuccessResult user,long workId){
		int count=zanMapper.countByWorkIdAndUserId(workId, user.getUserId());
		if(count<=0){
			TiMyworkszanlogs zanMod=new TiMyworkszanlogs();
			zanMod.setWorkid(workId);
			zanMod.setUserid(user.getUserId());
			zanMod.setUserimg(user.getHeadImg());
			zanMod.setNickname(user.getNickName());
			zanMod.setCreatetime(new Date()); 
			zanMapper.insert(zanMod);
		}
		return true;
	}
	/**
	 * 获取作品点赞列表
	 */
	public List<TiMyworkszanlogs> findZansList(long workId){
		List<TiMyworkszanlogs> list= zanMapper.findListByWorkId(workId);
		if(list!=null){
			for (TiMyworkszanlogs log : list) {
				log.setHeadimg(log.getUserimg());
			}
		}
		return list;
	}
}
