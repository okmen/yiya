package com.bbyiya.pic.service.ibs;

import java.util.List;

import com.bbyiya.pic.vo.order.OrderCountResultVO;
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
	ReturnModel find_payorderExtByUpUserid(Long userId,Integer status, String startTime,String endTime,int index,int size);
	/**
	 * IBSͳ��excel�����б�
	 * @param userId
	 * @param status
	 * @param startTime
	 * @param endTime
	 * @param index
	 * @param size
	 * @return
	 */
	List<OrderCountResultVO> find_ibsOrderExportExcelbyUpUserid(Long userId,
			Integer status, String startTime, String endTime, int index,
			int size);
	/**
	 * Ӱ¥�ڲ���ҵ��Ʒ�µ�ǰ�õ���Ʒ����ص�ַ
	 * @param userId
	 * @param cartid
	 * @return
	 */
	ReturnModel getMyProductAddressList(Long userId, Long cartid);
}
