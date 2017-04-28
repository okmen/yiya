package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.ReturnModel;

public interface ICts_OrderManageService {

	/**
	 * ��ȡӰ¥�Ƽ��˷�չ���û������б�
	 * @param branchuserId Ӱ¥ID
	 * @param startTimeStr ��ʼʱ��
	 * @param endTimeStr	����ʱ��
	 * @param status	״̬
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_payorderExtByBranchUpUserid(Long branchuserId,String startTimeStr,String endTimeStr,Integer status, int index,int size);
	/**
	 * ��ȡ΢���Ƽ��˷�չ���û������б�
	 * @param weiuserId
	 * @param startTimeStr
	 * @param endTimeStr
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_payorderExtByWeiUserUpUserid(Long weiuserId,
			String startTimeStr, String endTimeStr, Integer status, int index,
			int size);
}
