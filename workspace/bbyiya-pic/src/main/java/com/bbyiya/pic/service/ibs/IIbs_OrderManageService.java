package com.bbyiya.pic.service.ibs;

import com.bbyiya.vo.ReturnModel;

public interface IIbs_OrderManageService {

	/**
	 * �����Ƽ�userId��ȡ�����б�
	 * @param userId
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_payorderExtByUpUserid(Long userId,Integer status, int index,int size);
}
