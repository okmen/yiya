package com.bbyiya.service.impl;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.vo.user.RegisterParam;
import com.bbyiya.vo.user.UChildInfo;
import com.bbyiya.vo.user.UChildInfoParam;

@Service("userLoginService")
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class UserLoginService implements IUserLoginService{
	@Autowired
	private UOtherloginMapper otherloginMapper;
	@Autowired
	private UUsersMapper userDao;
	/**
	 * 日志对象
	 */
	private static Log log = LogFactory.getLog(UserLoginService.class); 
	/**
	 * 第三方登陆
	 * @param param
	 */
	public ReturnModel otherLogin(OtherLoginParam param)throws Exception{
		ReturnModel rq=new ReturnModel();
		if(param!=null){
			UOtherlogin others=otherloginMapper.get_UOtherlogin(param);
			if(others!=null){
				UUsers user =userDao.selectByPrimaryKey(others.getUserid());
				LoginSuccessResult loginSuccessResult=loginSuccess(user);
				if(others.getStatus()!=null&&others.getStatus().intValue()==1){
					if(user!=null){
						rq.setStatu(ReturnStatus.Success);
						rq.setBasemodle(loginSuccessResult);
					}else {
						rq.setStatu(ReturnStatus.SystemError);
						rq.setStatusreson("账号信息有误");
					}
				}else {
					loginSuccessResult.setStatus(0);//为绑定账户（没有设置密码） 
					rq.setBasemodle(loginSuccessResult);
				}
			}else {
				return otherRegiter(param);
			}
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数不能为空");
		}
		return rq;
	}
	
	/**
	 * 第三方用户注册
	 * @param param 第三方登陆 成功 返回信息 openid,headImg
	 * @return
	 */
	public ReturnModel otherRegiter(OtherLoginParam param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(param!=null){
			if(ObjectUtil.isEmpty(param.getOpenId())){
				rq.setStatusreson("openid不能为空");
				return rq;
			}
			if(param.getLoginType()==null){
				rq.setStatusreson("类型不能为空");
				return rq;
			}
			UUsers userModel=new UUsers();
			userModel.setCreatetime(new Date());
			userModel.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString())); 
			if(!ObjectUtil.isEmpty(param.getNickName()) ){
				userModel.setNickname(param.getNickName());
			}
			if(!ObjectUtil.isEmpty(param.getHeadImg())){
				userModel.setUserimg(param.getHeadImg());
			}
			userDao.insertReturnKeyId(userModel);
			
			UOtherlogin other=new UOtherlogin();
			other.setUserid(userModel.getUserid()); 
			other.setOpenid(param.getOpenId());
			other.setLogintype(param.getLoginType());
			other.setNickname(userModel.getNickname());
			other.setImage(param.getHeadImg());
			other.setStatus(userModel.getStatus());
			other.setCreatetime(new Date()); 
			otherloginMapper.insert(other);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("注册成功");
			rq.setBasemodle(loginSuccess(userModel));  
		}else {
			rq.setStatusreson("参数不能为空");
		}
		return rq;
	}
	
	public ReturnModel login(String userno, String pwd) throws Exception {
		ReturnModel rq = new ReturnModel();
		if (ObjectUtil.isEmpty(userno) || ObjectUtil.isEmpty(pwd)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
			return rq;
		}
		UUsers user =getUUser(userno); // userDao.getUUsersByPhone(userno);
		if (user != null && user.getPassword() != null && MD5Encrypt.encrypt(pwd).equals(user.getPassword())) {
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("成功");
			rq.setBasemodle(loginSuccess(user));
			return rq;
		}
		rq.setStatu(ReturnStatus.SystemError);
		rq.setStatusreson("用户名或密码错误！");
		log.error(userno+"用户名或密码错误"); 
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
		UUsers user = userDao.getUUsersByPhone(userno);
		if (user != null) {
			return user;
		}
		return null;
	}
	
	private LoginSuccessResult loginSuccess(UUsers user){
		if(user!=null){
			LoginSuccessResult result=new LoginSuccessResult();
			result.setUserId(user.getUserid());
			result.setIdentity(user.getIdentity());
			result.setMobilePhone(user.getMobilephone());
			result.setNickName(user.getNickname());
			result.setHeadImg(user.getUserimg());
			result.setStatus(user.getStatus()); 
			if(user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				//获取宝宝信息 
				UChildInfo child=new UChildInfo();
				
				result.setBabyInfo(child);
				
			}
			String s = UUID.randomUUID().toString();
			String ticket="WD"+s;
			RedisUtil.setObject(ticket, result,3600);
			result.setTicket(ticket);
			return result;
		}
		return null;
	}
	
	/**
	 * 
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public ReturnModel register(RegisterParam param) throws Exception {
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		if(param==null||ObjectUtil.isEmpty(param.getPassword())||ObjectUtil.isEmpty(param.getVcode())||ObjectUtil.isEmpty(param.getMobilephone())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
			return rq;
		}
		String key=param.getMobilephone()+"-"+Integer.parseInt(SendMsgEnums.register.toString());
		Object obj= RedisUtil.getObject(key);
		if(ObjectUtil.isEmpty(obj)||ObjectUtil.isEmpty(String.valueOf(obj))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("验证码已失效");
			return rq;
		}
		String vcode= String.valueOf(obj) ;
		if(!vcode.equals(param.getVcode())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("验证码有误");
			return rq;
		}
		UUsers model=new UUsers();
		model.setPassword(MD5Encrypt.encrypt(param.getPassword()));
		model.setCreatetime(new Date());
		model.setStatus(Integer.parseInt(UserStatusEnum.noChirlInfo.toString()));//等待设置宝宝信息
		if(!ObjectUtil.isEmpty(param.getUsername()) ){
//			if(!checkUser(param.getUsername(), 1)){
//				rq.setStatusreson("用户名已经存在");
//				return rq;
//			}
			model.setUsername(param.getUsername()); 
		}
		if(!ObjectUtil.isEmpty(param.getMobilephone())){
			model.setMobilephone(param.getMobilephone());
			model.setMobilebind(1); 
		} 
		userDao.insert(model);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(loginSuccess(model));
		return rq;
	}
	
	/**
	 * 设置孩子信息
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public ReturnModel addChildInfo(Long userId, UChildInfoParam param) throws Exception {
		ReturnModel rq=new ReturnModel();
		if(param==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
			return rq;
		}
		
		// 设置用户为完成体状态
		UUsers user= userDao.getUUsersByUserID(userId);
		user.setStatus(Integer.parseInt(UserStatusEnum.ok.toString()));
		userDao.updateByPrimaryKey(user);
		
		
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("成功"); 
		rq.setBasemodle(param);
		 
		return rq;
	}
	
	
	
	/**
	 * 
	 * @param userno
	 * @param type
	 * @return
	 */
	public boolean checkUser(String userno,int type){
		switch (type) {
		case 1://
			UUsers users=userDao.getUUsersByUserName(userno);
			if(users!=null){
				return false;
			}
			break;
		default:
			break;
		}
		return true;
	}
	
}
