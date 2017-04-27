package com.bbyiya.pic.service.pbs;

import java.util.List;

import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageInfo;

public interface IPbs_OrderMgtService {

	/**
	 * 查询订单列表（pbs用）
	 * 
	 * @param param
	 * @return
	 */
	PageInfo<PbsUserOrderResultVO> find_pbsOrderList(SearchOrderParam param,int index,int size);
	/** 
	 * 修改订单运单号及运费自动扣款
	 * @author Julie 
	 * @param orderId
	 * @param expressCom
	 * @param expressOrder
	 * @return
	 * @throws Exception
	 */
	ReturnModel editLogistics(String orderId, String expressCom,
			String expressOrder) throws Exception;
	
	String pbsdownloadImg(List<PbsUserOrderResultVO> orderlist);
	/**
	 * B端订单添加运费自动扣款
	 * @param orderId
	 * @param postage
	 * @return
	 * @throws Exception
	 */
	ReturnModel addPostage(String orderId, Double postage) throws Exception;
	ReturnModel isCanMergeOrderLogistic(String orderIds) throws Exception;
	ReturnModel MergeOrderLogistic(int ordertype, String orderIds,
			String expressCom, String expressOrder, Double postage)
			throws Exception;
	
}
