package com.bbyiya.dao;

import com.bbyiya.model.OOrderproducts;

public interface OOrderproductsMapper {
    int deleteByPrimaryKey(String orderproductid);

    int insert(OOrderproducts record);

    int insertSelective(OOrderproducts record);

    OOrderproducts selectByPrimaryKey(String orderproductid);

    int updateByPrimaryKeySelective(OOrderproducts record);

    int updateByPrimaryKey(OOrderproducts record);
}