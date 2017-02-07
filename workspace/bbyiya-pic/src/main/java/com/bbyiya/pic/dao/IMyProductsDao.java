package com.bbyiya.pic.dao;

import com.bbyiya.pic.vo.product.MyProductsResult;

public interface IMyProductsDao {

	/**
	 * 我的作品详情
	 * @param cartid
	 * @return
	 */
	MyProductsResult getMyProductResultVo(Long cartid);
}
