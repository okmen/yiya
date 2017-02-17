package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.MyProductsDetailsResult;

public interface IMyProductDetailsDao {

	/**
	 * ������ƷID��ȡ��Ʒ�����б�
	 * @param cartid
	 * @return
	 */
	List<MyProductsDetailsResult> findMyProductDetailsResult(@Param("cartId")Long cartid);
	/**
	 * ������ƷIdɾ�� ��Ʒ����
	 * @param cartId
	 */
	void deleMyProductDetailsByCartId(@Param("cartId")Long cartId);
}
