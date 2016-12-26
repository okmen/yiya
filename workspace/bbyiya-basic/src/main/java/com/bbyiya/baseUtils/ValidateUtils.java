package com.bbyiya.baseUtils;

import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.utils.ObjectUtil;


/**
 * 验证公用类
 * @author Administrator
 *
 */
public class ValidateUtils {

	/**
	 * 用户身份验证
	 * @param userIdentity 用户登录的身份标识
	 * @param type 需要验证的用户身份Identity
	 * @return
	 */
	public static boolean isIdentity(Long userIdentity,UserIdentityEnums type){
		long b=ObjectUtil.parseLong(type.toString());
		return b==(userIdentity&b);
	}
}
