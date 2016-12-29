package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.PProducts;
import com.bbyiya.vo.product.ProductResult;

public interface PProductsMapper {
    int deleteByPrimaryKey(Long productid);

    int insert(PProducts record);

    int insertSelective(PProducts record);

    PProducts selectByPrimaryKey(Long productid);

    int updateByPrimaryKeySelective(PProducts record);

    int updateByPrimaryKey(PProducts record);
    /**
     * 获取产品列表
     * @param branchUserId
     * @return
     */
    List<ProductResult> findProductResultByBranchUserId(Long branchUserId); 
    
    ProductResult getProductResultByProductId(Long productid);
}