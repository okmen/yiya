package com.bbyiya.service.impl;

import java.util.Date;
import java.util.UUID;



//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
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
	// private static Log log = LogFactory.getLog(UserLoginService.class);

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
			rq.setStatusreson("参数有误");
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
		if(!ObjectUtil.isNumberAlphaFix(userno)){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有非法字符！");
			return rq;
		}
		if (ObjectUtil.isEmpty(pwd)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("密码不能为空");
			return rq;
		}
		UUsers user = this.getUUser(userno);
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
		// 通过手机号获取用户信息
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

	/**
	 * 返回用户登陆信息
	 */
	public LoginSuccessResult loginSuccess(UUsers user) {
		if (user != null) {
			LoginSuccessResult result = getLoginSuccessResult_Common(user);
			String s = UUID.randomUUID().toString();
			String ticket = "YY" + s;
			RedisUtil.setObject(ticket, result, 86400);// 缓存一天
			result.setTicket(ticket); 
			return result;
		}
		return null;
	}

	
	public LoginSuccessResult getLoginSuccessResult_Common(UUsers user) {
		LoginSuccessResult result = new LoginSuccessResult();
		result.setUserId(user.getUserid());
		result.setIdentity(user.getIdentity());
		if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.agent)){
			result.setIsAgent(1);
		}
		if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)){
			result.setIsBranch(1);
		}
		if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.salesman)){
			result.setIsBranchMember(1);
		} 
		result.setMobilePhone(user.getMobilephone());
		result.setNickName(user.getNickname());
		result.setHeadImg(user.getUserimg());
		result.setStatus(user.getStatus());
		result.setSign(user.getSign());
		if (!ObjectUtil.isEmpty(user.getBirthday())) {
			result.setBirthday(DateUtil.getTimeStr(user.getBirthday(), "yyyy-MM-dd"));
		}
		// 完成注册// =》设置baby信息
		if (user.getStatus().intValue() == Integer.parseInt(UserStatusEnum.ok.toString())) {
			UChildreninfo childModel = childMapper.selectByPrimaryKey(user.getUserid());
			if (childModel != null) {
				// 获取宝宝信息
				UChildInfo child = new UChildInfo();
				if (childModel.getBirthday() != null) {
					child.setBirthdayStr(DateUtil.getTimeStr(childModel.getBirthday(), "yyyy-MM-dd HH:mm:ss"));
					child.setBirthday(childModel.getBirthday());
				}
				child.setNickName(childModel.getNickname());
				result.setBabyInfo(child);
				result.setHaveBabyInfo(1);// 已经填写宝宝信息
			}
		}
		return result;
	}

	/**
	 * 登录信息更新
	 * 
	 * @param user
	 * @param ticket_Old
	 * @return
	 */
	public LoginSuccessResult loginSuccess(UUsers user, String ticket_Old) {
		if (user != null) {
			LoginSuccessResult result = getLoginSuccessResult_Common(user);
			if (!ObjectUtil.isEmpty(ticket_Old)) {
				LoginSuccessResult old_loginUser = (LoginSuccessResult) RedisUtil.getObject(ticket_Old);
				if (old_loginUser != null) {
					RedisUtil.setObject(ticket_Old, result, 86400);// 缓存一天
					result.setTicket(ticket_Old);
					return result;
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
	 * 用户注册-》设置密码
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
		if(!ObjectUtil.validSqlStr(param.getPassword())){
			rq.setStatu(ReturnStatus.ParamError_2);
			rq.setStatusreson("密码包含有非法字符！");
			return rq;
		}
		// 手机验证码验证
		ResultMsg vResult = SendSMSByMobile.validateCode(param.getMobilephone(), param.getVcode(), SendMsgEnums.register);
		if (vResult.getStatus() != 1) {
			rq.setStatu(ReturnStatus.VcodeError_1);
			rq.setStatusreson(vResult.getMsg());
			return rq;
		}
		UUsers model = new UUsers();
		model.setPassword(MD5Encrypt.encrypt(param.getPassword()));
		model.setCreatetime(new Date());
		model.setStatus(Integer.parseInt(UserStatusEnum.ok.toString()));// 注册完成
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

	public LoginSuccessResult updateLoginSuccessResult(LoginSuccessResult user) {
		if (user != null && user.getUserId() != null) {
			UUsers userInfo = userDao.getUUsersByUserID(user.getUserId());
			if (userInfo != null) {
				return loginSuccess(userInfo, user.getTicket());
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
