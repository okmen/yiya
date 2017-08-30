package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiProducts;

public interface TiProductsMapper {
    int deleteByPrimaryKey(Long productid);

    int insert(TiProducts record);

    int insertSelective(TiProducts record);

    TiProducts selectByPrimaryKey(Long productid);

    int updateByPrimaryKeySelective(TiProducts record);

    int updateByPrimaryKey(TiProducts record);
    /**
     * 获取台历挂历年历产品列表
     * @return
     */
    List<TiProducts> findProductlist();
    
}