package com.bbyiya.service;

import com.bbyiya.vo.bigcase.BigcaseResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.github.pagehelper.PageInfo;

/**
 * 宝宝大事件
 * @author Administrator
 *
 */
public interface IBigCaseService {
	/**
	 * 宝宝大事件首页列表
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
//	PageInfo<BigcaseResult> find_MBigcaseResultPage(Long userId, int pageIndex, int pageSize);
	/**
	 * 宝宝大事件首页列表
	 * @param user
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	PageInfo<BigcaseResult> find_MBigcaseResultPage(LoginSuccessResult user, int pageIndex, int pageSize);
}
