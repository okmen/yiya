package com.bbyiya.pic.service.pbs;

import java.util.List;

import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.order.UserOrderResultVO;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageInfo;

public interface IPbs_OrderMgtService {

	/**
	 * ��ѯ�����б�pbs�ã�
	 * 
	 * @param param
	 * @return
	 */
	PageInfo<PbsUserOrderResultVO> find_pbsOrderList(SearchOrderParam param,int index,int size);
	/** 
	 * �޸Ķ����˵��ż��˷��Զ��ۿ�
	 * @author Julie 
	 * @param orderId
	 * @param expressCom
	 * @param expressOrder
	 * @return
	 * @throws Exception
	 */
	ReturnModel editLogistics(String orderId, String expressCom,
			String expressOrder,Double postage) throws Exception;
	
	String pbsdownloadImg(List<PbsUserOrderResultVO> orderlist);
	
}
