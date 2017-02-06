package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.vo.ReturnModel;


public interface IPic_ProductService {
	
	/**
	 * 获取产品样本详情
	 * @param productId
	 * @return
	 */
	ReturnModel getProductSamples(Long productId) ;
	/**
	 * 保存用户的作品 save user's product 
	 * @param param
	 * @return
	 */
	ReturnModel saveOrEdit_MyProducts(Long userId, MyProductParam param);
	/**
	 * 我的作品列表
	 * @param userId
	 * @return
	 */
	ReturnModel findMyProlist(Long userId);
	/**
	 * 获取作品详情
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfo(Long userId, Long cartId);
	/**
	 * 作品详情
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfo(Long cartId);
	/**
	 * 删除我的作品图片
	 * @param userId
	 * @param dpId
	 * @return
	 */
	ReturnModel del_myProductDetail(Long userId, Long dpId);
	
	/**
	 * 
	 * @param styleId
	 * @return
	 */
	ReturnModel getStyleCoordResult(Long styleId);
}
