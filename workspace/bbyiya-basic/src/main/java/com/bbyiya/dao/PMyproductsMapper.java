package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducts;
import com.bbyiya.vo.product.MyProductResultVo;

public interface PMyproductsMapper {
	int deleteByPrimaryKey(Long cartid);

	int insert(PMyproducts record);

	int insertSelective(PMyproducts record);

	/**
	 * 新增我的作品
	 * 
	 * @param cartid
	 * @return
	 */
	int insertReturnId(PMyproducts record);

	PMyproducts selectByPrimaryKey(Long cartid);

	int updateByPrimaryKeySelective(PMyproducts record);

	int updateByPrimaryKey(PMyproducts record);

	/**
	 * 获取用户作品列表
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	List<MyProductResultVo> findMyProductslist(@Param("userId") Long userId, @Param("status") Integer status);
	MyProductResultVo getMyProductResultVo(Long cartid);
}