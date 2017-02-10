package com.bbyiya.pic.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.pic.vo.product.MyProductsDetailsResult;

public interface IMyProductDetailsDao {

	List<MyProductsDetailsResult> findMyProductDetailsResult(@Param("cartId")Long cartid);
}
