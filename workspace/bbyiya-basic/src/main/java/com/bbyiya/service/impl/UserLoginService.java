package com.bbyiya.service.impl;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;

@Service("userLoginService")
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class UserLoginService implements IUserLoginService{
	@Autowired
	private UOtherloginMapper otherloginMapper;
	@Autowired
	private UUsersMapper userDao;
	
	
	public void otherLogin(OtherLoginParam param){
		
	}
	
	
	public ReturnModel login(String userno, String pwd) throws Exception {
		ReturnModel rq = new ReturnModel();
		if (ObjectUtil.isEmpty(userno) || ObjectUtil.isEmpty(pwd)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
			return rq;
		}
		UUsers user = getUUser(userno);
		if (user != null && user.getPassword() != null && MD5Encrypt.encrypt(pwd).equals(user.getPassword())) {
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("成功");
			rq.setBasemodle(loginSuccess(user));
			return rq;
		}
		rq.setStatu(ReturnStatus.SystemError);
		rq.setStatusreson("用户名或密码错误！");

		return rq;
	}
	
	
	/**
	 * 获取用户信息
	 * @param userno
	 * @return
	 */
	private UUsers getUUser(String userno) {
		Long userid = ObjectUtil.parseLong(userno);
		if (userid > 0) {//
			UUsers user = userDao.selectByPrimaryKey(userid);
			if (user != null) {
				return user;
			}
		}
		UUsers user = userDao.getUUsersByUserName(userno);
		if (user != null) {
			return user;
		}
		return null;
	}
	
	private LoginSuccessResult loginSuccess(UUsers user){
		if(user!=null){
			LoginSuccessResult result=new LoginSuccessResult();
			result.setUserid(user.getUserid());
			result.setUsername(user.getUsername());
			result.setCreatetime(user.getCreatetime());
			result.setEmail(user.getEmail());
			result.setIdentity(user.getIdentity());
			result.setMobilebind(user.getMobilebind());
			result.setMobilephone(user.getMobilephone());
			result.setNickname(user.getNickname());
			result.setUserimg(user.getUserimg());
			String s = UUID.randomUUID().toString();
			String ticket="WD"+s;
//			RedisUtil.setObject(ticket, result,3600);
			result.setTicket(ticket);
			return result;
		}
		return null;
	}
}
