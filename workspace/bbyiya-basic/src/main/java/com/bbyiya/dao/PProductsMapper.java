package com.bbyiya.dao;

import com.bbyiya.model.PProducts;

public interface PProductsMapper {
    int deleteByPrimaryKey(Long productid);

    int insert(PProducts record);

    int insertSelective(PProducts record);

    PProducts selectByPrimaryKey(Long productid);

    int updateByPrimaryKeySelective(PProducts record);

    int updateByPrimaryKey(PProducts record);
}