package com.bbyiya.pic.service.pbs;

import com.bbyiya.pic.vo.order.PbsSearchOrderParam;
import com.bbyiya.vo.ReturnModel;

public interface IPbs_OrderMgtService {

	/**
	 * ��ѯ�����б�pbs�ã�
	 * 
	 * @param param
	 * @return
	 */
	ReturnModel find_pbsOrderList(PbsSearchOrderParam param);

	
}
