package com.bbyiya.pic.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;

@Service("pic_userMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_UserMgtService implements IPic_UserMgtService {
	@Autowired
	private UOtherloginMapper otherloginMapper;
	@Autowired
	private UUsersMapper userDao;
	@Resource(name = "userLoginService")
	private IUserLoginService baseLoginService;

	/**
	 * ��������½
	 * 
	 * @param param
	 */
	public ReturnModel otherLogin(OtherLoginParam param) {
		ReturnModel rq = new ReturnModel();
		try {
			if (param != null) {
				UOtherlogin others = otherloginMapper.get_UOtherlogin(param);
				if (others != null) {
					UUsers user = userDao.selectByPrimaryKey(others.getUserid());
					LoginSuccessResult loginSuccessResult = null;
					if (user != null) {
						loginSuccessResult = baseLoginService.loginSuccess(user);
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
				rq.setStatusreson("��������Ϊ��");
			}
		} catch (Exception e) {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("ע��ʧ�ܣ�");
		}
		return rq;
	}

	/**
	 * �û�������ע��
	 * 
	 * @param param
	 * @return
	 */
	public ReturnModel otherRegiter(OtherLoginParam param) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if (param != null) {
			if (ObjectUtil.isEmpty(param.getOpenId())) {
				rq.setStatusreson("openid����Ϊ��");
				return rq;
			}
			if (param.getLoginType() == null) {
				rq.setStatusreson("���Ͳ���Ϊ��");
				return rq;
			}
			UOtherlogin other = otherloginMapper.get_UOtherlogin(param);
			if (other == null) {
				UUsers userModel = new UUsers();
				userModel.setCreatetime(new Date());
				userModel.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString()));
				userDao.insertReturnKeyId(userModel);

				other = new UOtherlogin();
				other.setUserid(userModel.getUserid());
				other.setOpenid(param.getOpenId());
				other.setLogintype(param.getLoginType());
				other.setNickname(param.getNickName());
				other.setImage(param.getHeadImg());
				other.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString()));
				other.setCreatetime(new Date());
				otherloginMapper.insert(other);
				LoginSuccessResult result = baseLoginService.loginSuccess(userModel);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("ע��ɹ�");
				rq.setBasemodle(result);
			} else if (other.getUserid() != null && other.getUserid() > 0) {
				UUsers userModel = userDao.getUUsersByUserID(other.getUserid());
				if (userModel != null) {
					LoginSuccessResult result = baseLoginService.loginSuccess(userModel);
					rq.setStatu(ReturnStatus.Success);
					rq.setStatusreson("��¼�ɹ�");
					rq.setBasemodle(result);
				}
			} else {
				//ע���û�
				UUsers userModel = new UUsers();
				userModel.setCreatetime(new Date());
				userModel.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString()));
				userDao.insertReturnKeyId(userModel);
				
				other.setUserid(userModel.getUserid());
				otherloginMapper.updateByPrimaryKeySelective(other);

				LoginSuccessResult result = baseLoginService.loginSuccess(userModel);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("��¼�ɹ�");
				rq.setBasemodle(result);
			}

		} else {
			rq.setStatusreson("��������");
		}
		return rq;
	}
}
