package com.bbyiya.pic.service.pbs;

import com.bbyiya.pic.vo.order.PbsSearchOrderParam;
import com.bbyiya.pic.vo.order.PbsUserOrderResultVO;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageInfo;

public interface IPbs_OrderMgtService {

	/**
	 * ��ѯ�����б�pbs�ã�
	 * 
	 * @param param
	 * @return
	 */
	PageInfo<PbsUserOrderResultVO> find_pbsOrderList(PbsSearchOrderParam param);

	
}
