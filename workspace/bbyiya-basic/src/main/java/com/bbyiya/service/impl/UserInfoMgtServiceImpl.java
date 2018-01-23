package com.bbyiya.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.enums.MsgStatusEnums;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUserresponsesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.model.UUserresponses;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UChildInfoParam;
import com.bbyiya.vo.user.UUserInfoParam;

@Service("userInfoMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UserInfoMgtServiceImpl implements IUserInfoMgtService {
	@Autowired
	private UUsersMapper userDao;
	@Autowired
	private UChildreninfoMapper childMapper;
	@Autowired
	private UUserresponsesMapper resposeMapper;
	
	@Resource(name = "userLoginService")
	private IUserLoginService loginService;

	public LoginSuccessResult getLoginSuccessResult(Long userId){
		UUsers user= userDao.selectByPrimaryKey(userId);
		if(user!=null){
			return loginService.getLoginSuccessResult_Common(user);
		}else {
			return null;
		}
	}
	
	public ReturnModel updatePWD(String mobile, String vcode, String pwd) {
		ReturnModel rq = new ReturnModel();
		if(!ObjectUtil.isNumberAlphaFix(mobile)){
			rq.setStatu(ReturnStatus.VcodeError_2);
			rq.setStatusreson("请填写正确的手机号！");
			return rq;
		}
		// 参数验证=========step1=====================
		if (ObjectUtil.isEmpty(vcode)) {
			rq.setStatu(ReturnStatus.VcodeError_2);
			rq.setStatusreson("验证码不能为空！");
			return rq;
		}
		if (ObjectUtil.isEmpty(pwd)) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("新密码不能为空！");
			return rq;
		}
		if(!ObjectUtil.validSqlStr(pwd)){
			rq.setStatu(ReturnStatus.ParamError_2);
			rq.setStatusreson("新密码包含有危险字符，请重新设置！");
			return rq;
		}
		// ----------------------------------
		// 手机验证码验证
		ResultMsg vResult = SendSMSByMobile.validateCode(mobile, vcode, SendMsgEnums.backPwd);
		if (vResult.getStatus() != Integer.parseInt(MsgStatusEnums.ok.toString())) {
			//验证码失效
			if(vResult.getStatus()==Integer.parseInt(MsgStatusEnums.invalid.toString())){
				rq.setStatu(ReturnStatus.VcodeError_1);
			}else {
				rq.setStatu(ReturnStatus.VcodeError_2);
			}
			rq.setStatusreson(vResult.getMsg());
			return rq;
		}
		// 重置密码
		UUsers users = userDao.getUUsersByPhone(mobile);
		if (users != null) {
			users.setStatus(Integer.parseInt(UserStatusEnum.ok.toString()));
			users.setPassword(MD5Encrypt.encrypt(pwd));
			userDao.updateByPrimaryKeySelective(users);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("成功");
			rq.setBasemodle(loginService.loginSuccess(users));
		} else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("该手机号未注册！");
		}
		return rq;
	}

	public ReturnModel addOrEdit_UChildreninfo(Long userId, UChildInfoParam param) {
		ReturnModel rq = new ReturnModel();
		if (param == null) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
			return rq;
		}
		try {
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
			if (!ObjectUtil.isEmpty(param.getNickName())) {
				if(!ObjectUtil.validSqlStr(param.getNickName())){ 
					rq.setStatu(ReturnStatus.ParamError_2);
					rq.setStatusreson("宝宝昵称有非法字符");
					return rq;
				}
				childModel.setNickname(param.getNickName());
			}
			if (!ObjectUtil.isEmpty(param.getBirthday())) {
				childModel.setBirthday(DateUtil.getDateByString("yyyy-mm-dd HH:mm:ss", param.getBirthday()));
			} else if (!havaBaby && ObjectUtil.isEmpty(param.getBirthday())) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("宝宝生日信息不能为空");
				return rq;
			}
			if (havaBaby) {// 修改宝宝信息
				childMapper.updateByPrimaryKey(childModel);
			} else { // 设置宝宝信息
				childMapper.insert(childModel);
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("成功");
		} catch (Exception e) {
			// TODO: handle exception
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("请检查宝宝信息是否有非法字符！");
		}
		return rq;
	}

	public ReturnModel editUUsers(Long userId, UUserInfoParam param) {
		ReturnModel rq = new ReturnModel();
		UUsers user = userDao.getUUsersByUserID(userId);
		if (user != null) {
			if (!ObjectUtil.isEmpty(param.getHeadImg())) {
				if(!ObjectUtil.validSqlStr(param.getHeadImg())){
					rq.setStatu(ReturnStatus.ParamError_2);
					rq.setStatusreson("有危险参数,请重新设置！");
					return rq;
				}
				user.setUserimg(param.getHeadImg());
			}
			if (!ObjectUtil.isEmpty(param.getNickName())) {
				param.setNickName(param.getNickName());
				
				if(!ObjectUtil.validSqlStr(param.getNickName())){
					rq.setStatu(ReturnStatus.ParamError_2);
					rq.setStatusreson("有危险参数,请重新设置！");
					return rq;
				}
				user.setNickname(param.getNickName());
			}
			if (!ObjectUtil.isEmpty(param.getSign())) {
				param.setSign(param.getSign());
				if(!ObjectUtil.validSqlStr(param.getSign())){
					rq.setStatu(ReturnStatus.ParamError_2);
					rq.setStatusreson("有危险参数,请重新设置！");
					return rq;
				}
				user.setSign(param.getSign());
			}
			if (!ObjectUtil.isEmpty(param.getBirthday())) {
				user.setBirthday(DateUtil.getDateByString("yyyy-MM-dd", param.getBirthday()));
			}
			userDao.updateByPrimaryKeySelective(user);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("修改成功！");
		} else {
			rq.setStatu(ReturnStatus.SystemError_1);
			rq.setStatusreson("用户不存在！");
		}
		return rq;
	}

	public ReturnModel add_UUserresponses(UUserresponses param) {
		ReturnModel rq = new ReturnModel();
		if (param == null || ObjectUtil.isEmpty(param.getUserid()) || ObjectUtil.isEmpty(param.getContent())) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
		} else {
			if(!ObjectUtil.validSqlStr(param.getContent())){
				rq.setStatu(ReturnStatus.ParamError_2);
				rq.setStatusreson("参数有危险字符，请重新填写！");
				return rq;
			}
			param.setCreatetime(new Date());
			resposeMapper.insert(param);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("反馈提交成功！");
		}
		return rq;
	}
}
