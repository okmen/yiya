package com.bbyiya.pic.service.calendar;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiEmployeeActOffVo;

public interface IIbs_TiPromoterEmployeeService {

	/**
	 * 添加推广者员工账户
	 * @param promoterUserId
	 * @param param
	 * @return
	 */
	ReturnModel addEmployeeUser(Long promoterUserId, TiEmployeeActOffVo param);
	/**
	 * 删除员工账号
	 * @param promoterUserId
	 * @param userId
	 * @return
	 */
	ReturnModel delEmployeeUser(Long promoterUserId, Long userId);
	/**
	 * 得到员工账号列表
	 * @param promoterId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findPromoterEmployeelistByPromoterId(Long promoterId,
			int index, int size)throws Exception;
	
	
	
}
