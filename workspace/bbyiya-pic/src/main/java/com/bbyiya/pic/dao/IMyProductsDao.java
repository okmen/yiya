package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.pic.vo.product.MyProductsResult;

public interface IMyProductsDao {

	/**
	 * 我的作品详情
	 * @param cartid
	 * @return
	 */
	MyProductsResult getMyProductResultVo(Long cartid);
	
	MyProductsResult getMyProductResultByProductId(@Param("userId") Long userId,@Param("productId")Long productId, @Param("status") Integer status);
	/**
	 * 我的作品列表
	 * (我的作品)
	 * v1.0
	 * @param userId
	 * @param phone
	 * @return
	 */
	List<MyProductListVo> findMyProductList(@Param("userId")Long userId,@Param("phone") String phone);
	/**
	 * 获取作品单个model 
	 * （我的作品）
	 *  v1.0
	 * @param cartId
	 * @return
	 */
	MyProductListVo getMyProductVO(@Param("cartid")Long cartId);
	/**
	 * 通过模板id获取作品列表
	 * @param tempid
	 * @return
	 */
	List<MyProductListVo>  getMyProductResultByTempId(@Param("tempid") Integer tempid);
	
	/**
	 * 根据作品模板，被邀请用户的userId获取被邀请的作品
	 * @param tempid
	 * @param userid
	 * @return
	 */
    MyProductListVo getMyProductByTempId(@Param("tempid")Integer tempid,@Param("userid")Long userid);
}
