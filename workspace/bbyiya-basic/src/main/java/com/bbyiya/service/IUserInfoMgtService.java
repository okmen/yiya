package com.bbyiya.service;

import com.bbyiya.model.UUserresponses;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UChildInfoParam;
import com.bbyiya.vo.user.UUserInfoParam;

/**
 * 用户信息处理类
 * 
 * @author Administrator
 *
 */
public interface IUserInfoMgtService {

	/**
	 * 用户修改登录密码 （通过手机号）
	 * @param mobile 手机号
	 * @param vcode 验证码
	 * @param pwd  新密码
	 * @return
	 */
	ReturnModel updatePWD(String mobile, String vcode, String pwd);

	/**
	 * 设置/更新 用户的孩子信息 
	 * @param userId 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel addOrEdit_UChildreninfo(Long userId, UChildInfoParam param);
	
	/**
	 * 修改用户资料
	 * @param userId
	 * @param param
	 */
	ReturnModel editUUsers(Long userId,UUserInfoParam param);
	/**
	 * 通过userId获取用户登录信息
	 * @param userId
	 * @return
	 */
	LoginSuccessResult getLoginSuccessResult(Long userId);
	/**
	 * 用户意见反馈
	 * @param param
	 * @return
	 */
	ReturnModel add_UUserresponses(UUserresponses param);
}
