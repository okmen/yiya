package com.bbyiya.service;

import com.bbyiya.enums.user.UserIdentityEnums;

public interface IBaseUserCommonService {

	/**
	 * 新增用户身份标识
	 * @param userId
	 * @param identity
	 */
	void addUserIdentity(Long userId,UserIdentityEnums identity);
	
	/**
	 * 移除用户身份标识
	 * @param userId
	 * @param identity
	 */
	void removeUserIdentity(Long userId,UserIdentityEnums identity);
}
