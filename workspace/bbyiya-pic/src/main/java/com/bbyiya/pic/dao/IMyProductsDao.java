package com.bbyiya.pic.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.MyProductsResult;

public interface IMyProductsDao {

	/**
	 * �ҵ���Ʒ����
	 * @param cartid
	 * @return
	 */
	MyProductsResult getMyProductResultVo(Long cartid);
	
	MyProductsResult getMyProductResultByProductId(@Param("userId") Long userId,@Param("productId")Long productId, @Param("status") Integer status);
}
