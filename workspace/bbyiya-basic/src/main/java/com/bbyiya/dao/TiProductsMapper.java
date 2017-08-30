package com.bbyiya.dao;

import com.bbyiya.model.TiProducts;

public interface TiProductsMapper {
    int deleteByPrimaryKey(Long productid);

    int insert(TiProducts record);

    int insertSelective(TiProducts record);

    TiProducts selectByPrimaryKey(Long productid);

    int updateByPrimaryKeySelective(TiProducts record);

    int updateByPrimaryKeyWithBLOBs(TiProducts record);

    int updateByPrimaryKey(TiProducts record);
}