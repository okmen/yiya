package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.vo.ReturnModel;
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
	/**
	 * 获取款式背景图片
	 * @param styleId
	 * @param ids
	 * @return
	 */
	ReturnModel find_previewsImg(long styleId,Integer[] ids);
	/**
	 * 获取预览款式背景图列表
	 * @param styleId
	 * @return
	 */
	ReturnModel find_previewsImg(long styleId);
	/**
	 * 获取款式列表
	 * @param styleId
	 * @return
	 */
	ReturnModel getStyleInfo(Long styleId);
}
