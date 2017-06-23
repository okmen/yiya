package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.ReturnModel;


public interface IScenseService {
	/**
	 * 添加或修改场影
	 * @param userid
	 * @param myScenseJson
	 * @return
	 */
	ReturnModel addorUpdateScense(Long userid, String myScenseJson);
	/**
	 * 获取场景列表
	 * @param index
	 * @param size
	 * @param keywords
	 * @param productId
	 * @return
	 */
	ReturnModel getScenseList(int index, int size, String keywords,
			String productId);
	
}