package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.MyProductsDetailsResult;

public interface IMyProductDetailsDao {

	/**
	 * 根据作品ID获取作品详情列表
	 * @param cartid
	 * @return
	 */
	List<MyProductsDetailsResult> findMyProductDetailsResult(@Param("cartId")Long cartid);
	/**
	 * 根据作品Id删除 作品详情
	 * @param cartId
	 */
	void deleMyProductDetailsByCartId(@Param("cartId")Long cartId);
}
