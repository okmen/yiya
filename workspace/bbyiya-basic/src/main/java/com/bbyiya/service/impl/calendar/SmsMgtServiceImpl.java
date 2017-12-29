package com.bbyiya.service.impl.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.model.TiActivitys;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.service.calendar.ISmsMgtService;
import com.bbyiya.utils.SendSMSByMobile;

@Service("sms_MgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class SmsMgtServiceImpl implements ISmsMgtService{
	@Autowired
	private TiActivitysMapper actMapper;
	@Autowired
	private TiMyworksMapper workMapper;
	@Autowired
	private TiActivityworksMapper activityworkMapper;
	
	/**
	 * 活动达标
	 * @param workId
	 * @return
	 */
	public boolean sendMsg_ActivityCompleteShare(long workId){
		TiActivityworks actworkInfo= activityworkMapper.selectByPrimaryKey(workId);
		if(actworkInfo!=null&&actworkInfo.getActid()!=null){
			TiActivitys act= actMapper.selectByPrimaryKey(actworkInfo.getActid());
			if(act!=null){
				SendSMSByMobile.batchSend(actworkInfo.getMobiephone(), "【咿呀科技】您参加的"+act.getTitle()+"，集赞数量已经达标啦！快打开活动，填写收货地址，等待礼品上门吧！");
			}
		}
		return true;
	}
}
