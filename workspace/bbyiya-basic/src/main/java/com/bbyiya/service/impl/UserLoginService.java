package com.bbyiya.service.impl;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.vo.user.RegisterParam;
import com.bbyiya.vo.user.UChildInfo;
import com.bbyiya.vo.user.UChildInfoParam;

@Service("userLoginService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UserLoginService implements IUserLoginService {
	@Autowired
	private UOtherloginMapper otherloginMapper;
	@Autowired
	private UUsersMapper userDao;
	@Autowired
	private UChildreninfoMapper childMapper;
	/**
	 * 日志对象
	 */
	private static Log log = LogFactory.getLog(UserLoginService.class);

	/**
	 * 第三方登陆
	 * 
	 * @param param
	 */
	public ReturnModel otherLogin(OtherLoginParam param) throws Exception {
		ReturnModel rq = new ReturnModel();
		if (param != null) {
			UOtherlogin others = otherloginMapper.get_UOtherlogin(param);
			if (others != null) {
				UUsers user = userDao.selectByPrimaryKey(others.getUserid());
				LoginSuccessResult loginSuccessResult = null;
				if (user != null) {
					loginSuccessResult = loginSuccess(user);
				} else {
					return otherRegiter(param);
				}
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(loginSuccessResult);
			} else {
				return otherRegiter(param);
			}
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数不能为空");
		}
		return rq;
	}
 
	public ReturnModel updatePWD(String mobile,String vcode,String pwd){
		ReturnModel rq=new ReturnModel();
		ResultMsg vResult= SendSMSByMobile.validateCode(mobile, vcode, SendMsgEnums.backPwd);
		if(vResult.getStatus()!=1){
			rq.setStatu(ReturnStatus.VcodeError_1);
			rq.setStatusreson(vResult.getMsg()); 
			return rq;
		}
		UUsers users= userDao.getUUsersByPhone(mobile);
		users.setPassword(MD5Encrypt.encrypt(pwd));
		userDao.updateByPrimaryKeySelective(users);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("成功");
		rq.setBasemodle(loginSuccess(users));  
		return rq;
	}
	
	/**
	 * 第三方用户注册
	 * 
	 * @param param
	 *            第三方登陆 成功 返回信息 openid,headImg
	 * @return
	 */
	public ReturnModel otherRegiter(OtherLoginParam param) {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if (param != null) {
			if (ObjectUtil.isEmpty(param.getOpenId())) {
				rq.setStatusreson("openid不能为空");
				return rq;
			}
			if (param.getLoginType() == null) {
				rq.setStatusreson("类型不能为空");
				return rq;
			}
			UOtherlogin other = otherloginMapper.get_UOtherlogin(param);
			if (other == null) {
				other = new UOtherlogin();
				other.setOpenid(param.getOpenId());
				other.setLogintype(param.getLoginType());
				other.setNickname(param.getNickName());
				other.setImage(param.getHeadImg());
				other.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString()));
				other.setCreatetime(new Date());
				otherloginMapper.insert(other);
			}
			String s = UUID.randomUUID().toString();
			String tokent = "YA" + s;
			RedisUtil.setObject(tokent, param, 3600);

			LoginSuccessResult result = new LoginSuccessResult();
			result.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString()));
			result.setRegister_token(tokent);

			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("注册成功");
			rq.setBasemodle(result);
		} else {
			rq.setStatusreson("参数不能为空");
		}
		return rq;
	}

	public ReturnModel login(String userno, String pwd) throws Exception {
		ReturnModel rq = new ReturnModel();
		if (ObjectUtil.isEmpty(userno)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("手机号不能为空");
			return rq;
		}
		if (ObjectUtil.isEmpty(pwd)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("密码不能为空");
			return rq;
		}
		UUsers user = getUUser(userno);
		if (user != null) {
			if (user.getPassword() != null && MD5Encrypt.encrypt(pwd).equals(user.getPassword())) {
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("登陆成功");
				rq.setBasemodle(loginSuccess(user));
				return rq;
			} else {
				rq.setStatu(ReturnStatus.LoginError_1);
				rq.setStatusreson("密码错误！");
				return rq;
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError_2);
			rq.setStatusreson("手机号未注册");
			return rq;
		}
	}

	/**
	 * 手机号/咿呀号 获取用户信息
	 * 
	 * @param userno
	 * @return
	 */
	private UUsers getUUser(String userno) {
		UUsers user = userDao.getUUsersByPhone(userno);
		if (user != null) {
			return user;
		}
		Long userid = ObjectUtil.parseLong(userno);
		if (userid > 0) {// 用户咿呀号
			user = userDao.selectByPrimaryKey(userid);
			if (user != null) {
				return user;
			}
		}
		return null;
	}

	private LoginSuccessResult loginSuccess(UUsers user) {
		if (user != null) {
			LoginSuccessResult result = new LoginSuccessResult();
			result.setUserId(user.getUserid());
			result.setIdentity(user.getIdentity());
			result.setMobilePhone(user.getMobilephone());
			result.setNickName(user.getNickname());
			result.setHeadImg(user.getUserimg());
			result.setStatus(user.getStatus());
			// 完成注册// =》设置baby信息
			if (user.getStatus().intValue() == Integer.parseInt(UserStatusEnum.ok.toString())) {
				UChildreninfo childModel = childMapper.selectByPrimaryKey(user.getUserid());
				if (childModel != null) {
					// 获取宝宝信息
					UChildInfo child = new UChildInfo();
					if(childModel.getBirthday()!=null){
						child.setBirthdayStr(DateUtil.getTimeStr(childModel.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
						child.setBirthday(childModel.getBirthday());
					}
					child.setNickName(childModel.getNickname());
					result.setBabyInfo(child);
					result.setHaveBabyInfo(1);// 已经填写宝宝信息
				}
			}
			String s = UUID.randomUUID().toString();
			String ticket = "YY" + s;
			RedisUtil.setObject(ticket, result, 86400);// 缓存一天
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
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		if (param == null || ObjectUtil.isEmpty(param.getPassword()) || ObjectUtil.isEmpty(param.getVcode()) || ObjectUtil.isEmpty(param.getMobilephone())) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数不全");
			return rq;
		}
		String key = param.getMobilephone() + "-" + Integer.parseInt(SendMsgEnums.register.toString());
		Object obj = RedisUtil.getObject(key);
		if (ObjectUtil.isEmpty(obj) || ObjectUtil.isEmpty(String.valueOf(obj))) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("验证码已失效");
			return rq;
		}
		String vcode = String.valueOf(obj);
		if (!vcode.equals(param.getVcode())) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("验证码有误");
			return rq;
		}
		UUsers model = new UUsers();
		model.setPassword(MD5Encrypt.encrypt(param.getPassword()));
		model.setCreatetime(new Date());
		model.setStatus(Integer.parseInt(UserStatusEnum.ok.toString()));// 注册完成
		if (!ObjectUtil.isEmpty(param.getUsername())) {
			model.setUsername(param.getUsername());
		}
		if (!ObjectUtil.isEmpty(param.getMobilephone())) {
			model.setMobilephone(param.getMobilephone());
			model.setMobilebind(1);
		}
		if (!ObjectUtil.isEmpty(param.getRegister_token())) {
			userDao.insertReturnKeyId(model);
			OtherLoginParam otherLogin = (OtherLoginParam) RedisUtil.getObject(param.getRegister_token());
			if (otherLogin != null && !ObjectUtil.isEmpty(otherLogin.getOpenId())) {
				UOtherlogin other = otherloginMapper.get_UOtherlogin(otherLogin);
				if (other != null) {
					other.setUserid(model.getUserid());
					otherloginMapper.updateByPrimaryKey(other);
				}
			}
		} else {
			userDao.insertReturnKeyId(model);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(loginSuccess(model));
		return rq;
	}

	/**
	 * 设置孩子信息
	 * 
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public ReturnModel addChildInfo(Long userId, UChildInfoParam param) throws Exception {
		ReturnModel rq = new ReturnModel();
		if (param == null) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
			return rq;
		}
		// 保存宝宝信息
		UChildreninfo childModel = childMapper.selectByPrimaryKey(userId);//
		boolean havaBaby = true;
		if (childModel == null) {
			childModel = new UChildreninfo();
			havaBaby = false;
			childModel.setUserid(userId);
			childModel.setCreatetime(new Date());
		}
		childModel.setSex(param.getSex());
		if(!ObjectUtil.isEmpty(param.getNickName())){
			childModel.setNickname(param.getNickName());
		}
		if(!ObjectUtil.isEmpty(param.getBirthday())){
			childModel.setBirthday(DateUtil.getDateByString("yyyy-mm-dd HH:mm:ss", param.getBirthday()));
		}else if(!havaBaby&&ObjectUtil.isEmpty(param.getBirthday())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("宝宝生日信息不能为空");
			return rq;
		}
		if (havaBaby) {// 修改宝宝信息
			childMapper.updateByPrimaryKey(childModel);
		} else { // 设置宝宝信息
			childMapper.insert(childModel);
//			// 设置用户为完成体状态
//			UUsers user = userDao.getUUsersByUserID(userId);
//			user.setStatus(Integer.parseInt(UserStatusEnum.ok.toString()));
//			userDao.updateByPrimaryKey(user);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("成功");
		return rq;
	}

	public LoginSuccessResult updateLoginSuccessResult(LoginSuccessResult user) {
		if (user != null && user.getUserId() != null) {
			UUsers userInfo = userDao.getUUsersByUserID(user.getUserId());
			if (userInfo != null) {
				return loginSuccess(userInfo);
			}
		}
		return user;
	}

	/**
	 * 
	 * @param userno
	 * @param type
	 * @return
	 */
	public boolean checkUser(String userno, int type) {
		switch (type) {
		case 1://
			UUsers users = userDao.getUUsersByUserName(userno);
			if (users != null) {
				return false;
			}
			break;
		default:
			break;
		}
		return true;
	}

}
