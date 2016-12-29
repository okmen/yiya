package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.vo.product.ProductResult;

public interface IBaseProductService {
	
	List<ProductResult> findProductList(Long userId);
}
