package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_myProductService {

	/**
	 * Эͬ�༭ ���뷢��
	 * @param userId
	 * @param phone
	 * @param cartId
	 * @return ReturnModel
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
	
	/**
	 * �ҵ���Ʒ�б�--���Լ�����Ʒ
	 * @param userId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_mycarts(Long userId,String phone,int index,int size);
	/**
	 * ������Ʒcartid��ȡ��Ʒ
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel get_mycart(Long userId, Long cartId);
	/**
	 * �ҵ���Ʒ�б�--���������Ʒ
	 * @param userId
	 * @return
	 */
//	ReturnModel find_mycartsInvited(Long userId);
	/**
	 * ����ɨ��ҳ��Ľ�������
	 * @param phone ���������ֻ���
	 * @param cartId ��Ʒcartid
	 * @param userId ���������û�ID
	 * @param vcode  ��֤��
	 * @param needVerfiCode  �Ƿ���Ҫ��֤�ֻ���֤�� 0 ����Ҫ��1��Ҫ
	 * @author julie at 2017-04-26
	 * @throws Exception
	 */
	ReturnModel acceptScanQrCodeInvite(Long userId,String phone, Long cartId, String vcode, Integer needVerfiCode);
	
}
