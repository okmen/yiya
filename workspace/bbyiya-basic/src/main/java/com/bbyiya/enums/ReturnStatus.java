package com.bbyiya.enums;

public enum ReturnStatus {
	/**
	 * 操作成功
	 */
	Success(1),
	/*-----------------------------------登陆错误--------------------------------------------*/
	/**
	 * 登陆过期（需要重新登陆）
	 */
	LoginError(-1),
	/**
	 * 登陆 密码错误
	 */
	LoginError_1(401),
	/**
	 * 手机号未注册
	 */
	LoginError_2(402),
	/*-----------------------------------参数错误--------------------------------------------*/
	/**
	 * 参数错误
	 */
	ParamError(201),
	/*-----------------------------------系统错误--------------------------------------------*/
	/**
	 * 系统错误
	 */
	SystemError(301),
	
	/*-----------------------------------验证码错误--------------------------------------------*/
	/**
	 * 验证码失效
	 */
	VcodeError_1(101);

	private final int step;

	private ReturnStatus(int step) {

		this.step = step;
	}

	public int getValue() {
		return step;
	}

	@Override
	public String toString() {
		return String.valueOf(this.step);
	}
}
