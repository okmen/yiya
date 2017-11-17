package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiProducts;
import com.bbyiya.vo.calendar.product.TiProductResult;

public interface TiProductsMapper {
    int deleteByPrimaryKey(Long productid);

    int insert(TiProducts record);

    int insertSelective(TiProducts record);

    TiProducts selectByPrimaryKey(Long productid);
   

    int updateByPrimaryKeySelective(TiProducts record);

    int updateByPrimaryKey(TiProducts record);
    
    /**
     * 获取model
     * @param productid
     * @return
     */
    TiProductResult getResultByProductId(Long productid);
    /**
     * 获取台历挂历年历产品列表
     * @return
     */
    List<TiProducts> findProductlist();
    /**
     * 
     * @return
     */
    List<TiProductResult> findProductResultlist();
    
    List<TiProducts> findProductlistByProductIds(@Param("productIds")List<Long> productIds);
}