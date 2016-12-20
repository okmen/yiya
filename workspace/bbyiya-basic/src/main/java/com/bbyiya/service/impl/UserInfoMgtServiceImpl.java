package com.bbyiya.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UChildreninfo;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.utils.encrypt.MD5Encrypt;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.UChildInfoParam;

@Service("userInfoMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UserInfoMgtServiceImpl implements IUserInfoMgtService {
	@Autowired
	private UUsersMapper userDao;
	@Autowired
	private UChildreninfoMapper childMapper;

	@Resource(name = "userLoginService")
	private IUserLoginService loginService;

	
	public ReturnModel updatePWD(String mobile, String vcode, String pwd) {
		ReturnModel rq = new ReturnModel();
		//参数验证=========step1=====================
		if(ObjectUtil.isEmpty(vcode)){
			rq.setStatu(ReturnStatus.VcodeError_2);
			rq.setStatusreson("验证码不能为空！");
			return rq;
		}
		if(ObjectUtil.isEmpty(pwd)){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("新密码不能为空！");
			return rq;
		}
		//----------------------------------
		//手机验证码验证
		ResultMsg vResult = SendSMSByMobile.validateCode(mobile, vcode, SendMsgEnums.backPwd);
		if (vResult.getStatus() != 1) {
			rq.setStatu(ReturnStatus.VcodeError_1);
			rq.setStatusreson(vResult.getMsg());
			return rq;
		}
		//重置密码
		UUsers users = userDao.getUUsersByPhone(mobile);
		if(users!=null){
			users.setPassword(MD5Encrypt.encrypt(pwd));
			userDao.updateByPrimaryKeySelective(users);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("成功");
			rq.setBasemodle(loginService.loginSuccess(users));
		}else {
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
				childModel.setNickname(param.getNickName());
			}
			if (!ObjectUtil.isEmpty(param.getBirthday())) {
				childModel.setBirthday(DateUtil.getDateByString("yyyy-mm-dd HH:mm:ss", param.getBirthday()));
			} else if (!havaBaby && ObjectUtil.isEmpty(param.getBirthday())) {
				rq.setStatu(ReturnStatus.ParamError);
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
			rq.setStatusreson(e.getMessage());
		}
		return rq;
	}
}
