package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.model.DMyproductdiscountmodel;

public interface IBaseDiscountService {
	/**
	 * 我的作品优惠
	 * @param userId
	 * @param cartId
	 * @return
	 */
	List<DMyproductdiscountmodel> findMycartDiscount(long userId,Long cartId);
	/**
	 * 给活动结束未获得名额的作品 优惠
	 * @param cartId
	 */
	void addTempDiscount(Long cartId) ;
}
