package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.ProductSampleResultVO;

public interface IPic_ProductDao {

	/**
	 *  获取相册样本列表
	 * @param productId
	 * @return
	 */
	List<ProductSampleResultVO> findProductSamplesByProductId(@Param("productId")Long productId);
}
