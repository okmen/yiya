package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.ProductSampleResultVO;

public interface IPic_ProductDao {

	/**
	 *  ��ȡ��������б�
	 * @param productId
	 * @return
	 */
	List<ProductSampleResultVO> findProductSamplesByProductId(@Param("productId")Long productId);
}
