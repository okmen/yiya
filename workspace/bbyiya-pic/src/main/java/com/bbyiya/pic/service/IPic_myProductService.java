package com.bbyiya.pic.service;

import com.bbyiya.model.PMyproducts;
import com.bbyiya.vo.ReturnModel;

public interface IPic_myProductService {

	/**
	 * 协同编辑 邀请发送
	 * @param userId
	 * @param phone
	 * @param cartId
	 * @return ReturnModel
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
	 * 协同邀请 处理
	 * @param cartId
	 * @param userId
	 * @param status
	 * @return
	 */
	ReturnModel processInvite(Long cartId,Long userId, int status);
	
	/**
	 * 我的个人提示信息
	 * @param userId
	 * @param mobilePhone
	 * @return
	 */
	ReturnModel  myUserInfoExp(Long userId,String mobilePhone);
	
	/**
	 * 我的作品列表--我自己的作品
	 * @param userId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_mycarts(Long userId,String phone,int index,int size);
	/**
	 * 根据作品cartid获取作品
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel get_mycart(Long userId, Long cartId);
	/**
	 * 我的作品列表--被邀请的作品
	 * @param userId
	 * @return
	 */
//	ReturnModel find_mycartsInvited(Long userId);
	/**
	 * 处理扫码页面的接受邀请
	 * @param phone 被邀请人手机号
	 * @param cartId 作品cartid
	 * @param userId 被邀请人用户ID
	 * @param vcode  验证码
	 * @param needVerfiCode  是否需要验证手机验证码 0 不需要，1需要
	 * @param version  二维码版本号
	 * @author julie at 2017-04-26
	 * @throws Exception
	 */
	ReturnModel acceptScanQrCodeInvite(Long userId,String phone, Long cartId, String vcode, Integer needVerfiCode,String version);
	/**
	 * 处理医院扫码页面的接受邀请
	 * @param userId
	 * @param phone
	 * @param cartId
	 * @return
	 */
	ReturnModel acceptTempScanQrCodeInvite(Long userId, String phone,
			Long cartId,String vcode,Integer needVerfiCode);
	/**
	 * 根据cartId得到作品
	 * @param cartId
	 * @return
	 */
	PMyproducts getPMyproducts(Long cartId);
	
}
