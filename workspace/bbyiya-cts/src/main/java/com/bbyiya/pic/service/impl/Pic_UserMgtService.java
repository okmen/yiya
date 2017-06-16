package com.bbyiya.pic.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.common.enums.SendMsgEnums;
import com.bbyiya.common.vo.ResultMsg;
import com.bbyiya.dao.UOtherloginMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.dao.UUsertesterwxMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UOtherlogin;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.service.IUserLoginService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.SendSMSByMobile;
import com.bbyiya.utils.encrypt.MD5Encrypt;
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

	@Autowired
	private UUsertesterwxMapper testerMapper;
	
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
						boolean edit=false;
						if(ObjectUtil.isEmpty(user.getNickname())||"null".equals(user.getNickname())){
							if(!ObjectUtil.isEmpty(param.getNickName())&&!"null".equals(param.getNickName())){
								user.setNickname(param.getNickName());
								edit=true;	
							}
						}
						if(ObjectUtil.isEmpty(user.getUserimg())||"null".equals(user.getUserimg())){
							if(!ObjectUtil.isEmpty(param.getHeadImg())&&!"null".equals(param.getHeadImg())){
								user.setUserimg(param.getHeadImg());
								edit=true;
							}
						} 
						loginSuccessResult = baseLoginService.loginSuccess(user);
						if(edit){ 
							userDao.updateByPrimaryKeySelective(user);
						}
						
					} else {
						return otherRegiter(param,others);
					}
					rq.setStatu(ReturnStatus.Success);
					rq.setBasemodle(loginSuccessResult);
				} else {
					return otherRegiter(param,others);
				}
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��������Ϊ��");
			}
		} catch (Exception e) {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("ע��ʧ�ܣ�");
			rq.setBasemodle(e); 
		}
		return rq;
	}

	/**
	 * �û�������ע��
	 * @param other
	 * @param param
	 * @return
	 */
	public ReturnModel otherRegiter(OtherLoginParam param,UOtherlogin other) throws Exception {
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
			if (other == null) {
				UUsers userModel = new UUsers();
				userModel.setCreatetime(new Date());
				userModel.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString()));
				if(!ObjectUtil.isEmpty(param.getNickName())){
					userModel.setNickname(param.getNickName());
				}
				if(!ObjectUtil.isEmpty(param.getHeadImg())){
					userModel.setUserimg(param.getHeadImg());
				} 
				if(param.getUpUserId()!=null&&param.getUpUserId()>0){
					UUsers upUsers= userDao.getUUsersByUserID(param.getUpUserId());
					if(upUsers!=null){
						userModel.setUpuserid(param.getUpUserId()); 
						//����Ƽ�����������
						if(ValidateUtils.isIdentity(upUsers.getUserid(), UserIdentityEnums.branch)||
								ValidateUtils.isIdentity(upUsers.getUserid(), UserIdentityEnums.wei)){
							userModel.setSourseuserid(upUsers.getUserid());
						}
						//����Ƽ��˵��ϼ���b�˻�������
						else if (ValidateUtils.isIdentity(upUsers.getUpuserid(), UserIdentityEnums.branch)||
								ValidateUtils.isIdentity(upUsers.getUpuserid(), UserIdentityEnums.wei)){
							userModel.setSourseuserid(upUsers.getUpuserid());
						}
						//����Ƽ����ж����Ƽ���
						else if (upUsers.getSourseuserid()!=null&&upUsers.getSourseuserid()>0) {
							userModel.setSourseuserid(upUsers.getSourseuserid()); 
						}
					}
				}
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
			} 
			else if (other.getUserid() != null && other.getUserid() > 0) {
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
	
	public ReturnModel bindMobilephone(Long userId,String phone,String vcode){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		ResultMsg msgResult= SendSMSByMobile.validateCode(phone, vcode, SendMsgEnums.register);
		if(msgResult.getStatus()==1) {
			UUsers userPhone=userDao.getUUsersByPhone(phone);
			if(userPhone!=null&&userPhone.getUserid().longValue()!=userId){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("���ֻ����Ѿ��������û���");
				return rq;
			}
			UUsers user= userDao.getUUsersByUserID(userId);
			if(user!=null){
				user.setMobilephone(phone);
				user.setStatus(Integer.parseInt(UserStatusEnum.noPwd.toString())); 
				userDao.updateByPrimaryKey(user);
				LoginSuccessResult result = baseLoginService.getLoginSuccessResult_Common(user);
				rq.setStatu(ReturnStatus.Success); 
				rq.setBasemodle(result); 
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("ϵͳ����");
			}
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson(msgResult.getMsg()); 
		}
		return rq;
		
	}
	
	public ReturnModel setPwd(Long userId,String pwd,String phone,String vcode){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(ObjectUtil.isEmpty(pwd)){
			rq.setStatusreson("���벻��Ϊ��");
			return rq;
		}
		if(!ObjectUtil.validSqlStr(pwd)){
			rq.setStatusreson("�����з��գ��������ã�");
			return rq;
		}
		ResultMsg msgResult= SendSMSByMobile.validateCode(phone, vcode, SendMsgEnums.register);
		if(msgResult.getStatus()==1) {
			UUsers userPhone=userDao.getUUsersByPhone(phone);
			if(userPhone!=null&&userPhone.getUserid().longValue()!=userId){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("���ֻ����Ѿ��������û���");
				return rq;
			}
			UUsers user= userDao.getUUsersByUserID(userId);
			if(user!=null){
				user.setMobilephone(phone);
				user.setStatus(Integer.parseInt(UserStatusEnum.ok.toString())); 
				user.setPassword(MD5Encrypt.encrypt(pwd)); 
				userDao.updateByPrimaryKey(user);
				LoginSuccessResult result = baseLoginService.getLoginSuccessResult_Common(user);
				rq.setStatu(ReturnStatus.Success); 
				rq.setBasemodle(result); 
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("ϵͳ����");
			}
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson(msgResult.getMsg()); 
		}
		return rq;
		
	}
}
