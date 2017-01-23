package com.bbyiya.dao;

import com.bbyiya.model.PMyproducts;

public interface PMyproductsMapper {
    int deleteByPrimaryKey(Long cartid);

    int insert(PMyproducts record);

    int insertSelective(PMyproducts record);

    PMyproducts selectByPrimaryKey(Long cartid);

    int updateByPrimaryKeySelective(PMyproducts record);

    int updateByPrimaryKey(PMyproducts record);
}