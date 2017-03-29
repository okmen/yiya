package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.MyProductListVo;
import com.bbyiya.pic.vo.product.MyProductsResult;

public interface IMyProductsDao {

	/**
	 * �ҵ���Ʒ����
	 * @param cartid
	 * @return
	 */
	MyProductsResult getMyProductResultVo(Long cartid);
	
	MyProductsResult getMyProductResultByProductId(@Param("userId") Long userId,@Param("productId")Long productId, @Param("status") Integer status);
	/**
	 * �ҵ���Ʒ�б�
	 * @param userId
	 * @param phone
	 * @return
	 */
	List<MyProductListVo> findMyProductList(@Param("userId")Long userId,@Param("phone") String phone);

}
