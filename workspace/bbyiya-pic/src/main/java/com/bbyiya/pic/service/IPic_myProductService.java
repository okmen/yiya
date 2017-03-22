package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_myProductService {

	/**
	 * 协同编辑 邀请发送
	 * @param userId
	 * @param phone
	 * @param cartId
	 * @return
	 */
	ReturnModel sendInvite(Long userId, String phone,Long cartId);
	/**
	 * 协同邀请 处理
	 * @param phone
	 * @param cartId
	 * @param status
	 * @return
	 */
	ReturnModel processInvite(String phone, Long cartId, int status);
	
	/**
	 * 我的个人提示信息
	 * @param userId
	 * @param mobilePhone
	 * @return
	 */
	ReturnModel  myUserInfoExp(Long userId,String mobilePhone);
	
}
