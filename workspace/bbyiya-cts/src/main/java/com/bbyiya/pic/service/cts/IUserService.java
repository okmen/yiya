package com.bbyiya.pic.service.cts;

import com.bbyiya.model.UUsers;
import com.bbyiya.vo.ReturnModel;


public interface IUserService {
	/**
	 * 添加cts内部账号
	 * @param userid
	 * @param phone
	 * @return
	 */
	ReturnModel addCtsUser(Long userid, String phone);
	/**
	 * 删除cts内部账号
	 * */
	ReturnModel deleteCtsUser(Long userid);
	/**
	 * 内部员工账号列表
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findCtsMemberlist(String keywords, int index, int size);
	/**
	 * 手机号/咿呀号 获取用户信息
	 * 
	 * @param userno
	 * @return
	 */
	ReturnModel getUserInfo(String userno);
	
	
}