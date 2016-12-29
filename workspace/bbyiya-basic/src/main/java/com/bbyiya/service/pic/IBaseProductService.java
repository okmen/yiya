package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.vo.product.ProductResult;

public interface IBaseProductService {

	/**
	 * 获取相册列表
	 * @param userId
	 * @return
	 */
	List<ProductResult> findProductList(Long userId);

	/**
	 * 获取产品详情信息
	 * @param producId
	 * @return
	 */
	ProductResult getProductResult(Long producId);
}
