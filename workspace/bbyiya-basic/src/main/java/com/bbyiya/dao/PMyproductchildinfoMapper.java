package com.bbyiya.dao;

import com.bbyiya.model.PMyproductchildinfo;

public interface PMyproductchildinfoMapper {
    int deleteByPrimaryKey(Long cartid);

    int insert(PMyproductchildinfo record);

    int insertSelective(PMyproductchildinfo record);

    PMyproductchildinfo selectByPrimaryKey(Long cartid);

    int updateByPrimaryKeySelective(PMyproductchildinfo record);

    int updateByPrimaryKey(PMyproductchildinfo record);
}