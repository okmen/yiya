package com.bbyiya.cts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.SmsParam;
import com.bbyiya.cts.service.ISmsRemindUserService;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.model.UUsers;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.vo.user.SmsRemindUserVo;

@Service("smsRemindUserService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class SmsRemindUserServiceImpl implements ISmsRemindUserService{
	
	private Logger Log = Logger.getLogger(SmsRemindUserServiceImpl.class);
	
	
	@Autowired
	private UAccountsMapper accountMapper;
	@Autowired
	private UUsersMapper usersMapper;
	
	private String keyString="smsReminUser";
	
	/**
	 * 短信通知用户充值
	 * @return
	 */
	public void smsReminUserToChongzhi(){
		getSmsRemindUsers();
		List<SmsRemindUserVo> userlist= (List<SmsRemindUserVo>) RedisUtil.getObject(keyString);
		if(userlist==null||userlist.size()<=0){
			Log.info("暂无需通知的用户！");
		}else{
			for (SmsRemindUserVo uservo : userlist) {
				if(uservo.getIssend()!=null&&uservo.getIssend().intValue()==0){
					UUsers user=usersMapper.getUUsersByUserID(uservo.getUserid());
					if(!ObjectUtil.isEmpty(user.getMobilephone())){
						SmsParam sendparam=new SmsParam();
						sendparam.setUserId(user.getUserid());
						SendSMSByMobile.sendSmS(Integer.parseInt(SendMsgEnums.remind_chongzhi.toString()),user.getMobilephone(), sendparam);
						Log.info("用户ID号为["+user.getUserid()+"],手机号为["+user.getMobilephone()+"]的账号发送短信充值通知成功！");
					}
					uservo.setIssend(1);
				}
			}
			//重新更新缓存
			RedisUtil.setObject(keyString, userlist);
		}
		
	}
	
	/**
	 * 处理缓存用户
	 */
	public void getSmsRemindUsers(){
		
		List<SmsRemindUserVo> cacheuserlist= (List<SmsRemindUserVo>) RedisUtil.getObject(keyString);
		//查询最新余额小于500的所有用户
		List<SmsRemindUserVo> newuserlist=accountMapper.findSmsRemindUserList(500.0);
		List<SmsRemindUserVo> newcacheuserlist=new ArrayList<SmsRemindUserVo>();
		//初始的情况
		if(cacheuserlist==null){
			cacheuserlist=new ArrayList<SmsRemindUserVo>();			
			if(newuserlist!=null&&newuserlist.size()>0){
				for (SmsRemindUserVo user : newuserlist) {
					SmsRemindUserVo uservo=new SmsRemindUserVo();
					uservo.setUserid(user.getUserid());
					uservo.setIssend(0);
					cacheuserlist.add(uservo);
				}
			}
			RedisUtil.setObject(keyString, cacheuserlist);
		}else if(newuserlist!=null&&newuserlist.size()>0){
			for (SmsRemindUserVo usernew : newuserlist) {
				SmsRemindUserVo uservo=new SmsRemindUserVo();
				uservo.setUserid(usernew.getUserid());
				if(cacheuserlist!=null&&cacheuserlist.size()>0){
					for (SmsRemindUserVo cacheuser : cacheuserlist) {
						if(usernew.getUserid().longValue()==cacheuser.getUserid().longValue()){
							uservo.setIssend(cacheuser.getIssend());
							break;
						}
					}
				}
				newcacheuserlist.add(uservo);
			}
			RedisUtil.delete(keyString);
			RedisUtil.setObject(keyString, newcacheuserlist);
		}
	}
}
