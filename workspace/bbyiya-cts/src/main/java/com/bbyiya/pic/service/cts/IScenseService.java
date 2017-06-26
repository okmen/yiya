package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.ReturnModel;


public interface IScenseService {
	/**
	 * ��ӻ��޸ĳ�Ӱ
	 * @param userid
	 * @param myScenseJson
	 * @return
	 */
	ReturnModel addorUpdateScense(Long userid, String myScenseJson);
	/**
	 * ��ȡ�����б�
	 * @param index
	 * @param size
	 * @param keywords
	 * @param productId
	 * @return
	 */
	ReturnModel getScenseList(int index, int size, String keywords,
			String productId);
	
}