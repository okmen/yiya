package com.bbyiya.service;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.OtherLoginParam;
import com.bbyiya.vo.user.RegisterParam;
import com.bbyiya.vo.user.UChildInfoParam;

public interface IUserLoginService {

	/**
	 * 用户登陆（手机号/用户名 密码登陆）
	 * @param userno
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	ReturnModel login(String userno, String pwd) throws Exception;
	/**
	 * 用户注册
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel register(RegisterParam param) throws Exception ;
	/**
	 * 设置用户的孩子信息
	 * @param userId
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel addChildInfo(Long userId, UChildInfoParam param) throws Exception;
	/**
	 * 更新用户登陆信息
	 * @param user 此操作之前的用户登陆信息 the user's loginInfo before
	 * @return
	 */
	LoginSuccessResult updateLoginSuccessResult(LoginSuccessResult user);
	/**
	 * 第三方登陆（未注册的直接注册登陆）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel otherLogin(OtherLoginParam param)throws Exception;
	
}
