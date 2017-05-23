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
	 * (�ҵ���Ʒ)
	 * v1.0
	 * @param userId
	 * @param phone
	 * @return
	 */
	List<MyProductListVo> findMyProductList(@Param("userId")Long userId,@Param("phone") String phone);
	/**
	 * ��ȡ��Ʒ����model 
	 * ���ҵ���Ʒ��
	 *  v1.0
	 * @param cartId
	 * @return
	 */
	MyProductListVo getMyProductVO(@Param("cartid")Long cartId);
	/**
	 * ͨ��ģ��id��ȡ��Ʒ�б�
	 * @param tempid
	 * @return
	 */
	List<MyProductListVo>  getMyProductResultByTempId(@Param("tempid") Integer tempid);
	
	/**
	 * ������Ʒģ�壬�������û���userId��ȡ���������Ʒ
	 * @param tempid
	 * @param userid
	 * @return
	 */
    MyProductListVo getMyProductByTempId(@Param("tempid")Integer tempid,@Param("userid")Long userid);
}
