package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_myProductService {

	/**
	 * Эͬ�༭ ���뷢��
	 * @param userId
	 * @param phone
	 * @param cartId
	 * @return
	 */
	ReturnModel sendInvite(Long userId, String phone,Long cartId);
	/**
	 * Эͬ���� ����
	 * @param phone
	 * @param cartId
	 * @param status
	 * @return
	 */
	ReturnModel processInvite(String phone, Long cartId, int status);
	
	/**
	 * �ҵĸ�����ʾ��Ϣ
	 * @param userId
	 * @param mobilePhone
	 * @return
	 */
	ReturnModel  myUserInfoExp(Long userId,String mobilePhone);
}