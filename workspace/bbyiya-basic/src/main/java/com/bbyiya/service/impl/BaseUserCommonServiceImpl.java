package com.bbyiya.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IBaseUserCommonService;

@Service("baseUserCommon")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseUserCommonServiceImpl implements IBaseUserCommonService{
	@Autowired
	private UUsersMapper usersMapper;
	
	/**
	 * 更新用户身份标识
	 * @param userId
	 * @param identity
	 */
	public void addUserIdentity(Long userId,UserIdentityEnums identity){
		UUsers users=usersMapper.selectByPrimaryKey(userId);
		if(users!=null){
			long ident=users.getIdentity()==null?0:users.getIdentity();
			if(!ValidateUtils.isIdentity(ident, identity)){
				users.setIdentity(ident+Long.parseLong(identity.toString()));
				usersMapper.updateByPrimaryKeySelective(users);
			}
		}
	}
	
	public void removeUserIdentity(Long userId,UserIdentityEnums identity){
		UUsers users=usersMapper.selectByPrimaryKey(userId);
		if(users!=null){
			long ident=users.getIdentity()==null?0:users.getIdentity();
			if(ValidateUtils.isIdentity(ident, identity)){
				users.setIdentity(ident-Long.parseLong(identity.toString()));
				usersMapper.updateByPrimaryKeySelective(users);
			}
		}
	}
}
