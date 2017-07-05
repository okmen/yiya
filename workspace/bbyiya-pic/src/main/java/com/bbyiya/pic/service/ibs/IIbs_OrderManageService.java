package com.bbyiya.pic.service.ibs;

import java.util.List;

import com.bbyiya.pic.vo.order.OrderCountResultVO;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_OrderManageService {

	/**
	 * 根据推荐userId获取订单列表
	 * @param userId
	 * @param status
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel find_payorderExtByUpUserid(Long userId,Integer status, String startTime,String endTime,int index,int size);
	/**
	 * IBS统计excel导出列表
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
	 * 影楼内部异业作品下单前得到作品的相关地址
	 * @param userId
	 * @param cartid
	 * @return
	 */
	ReturnModel getMyProductAddressList(Long userId, Long cartid);
}
