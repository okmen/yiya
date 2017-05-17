package com.bbyiya.dao;

import com.bbyiya.model.PMyproductext;

public interface PMyproductextMapper {
    int deleteByPrimaryKey(Long cartid);

    int insert(PMyproductext record);

    int insertSelective(PMyproductext record);

    PMyproductext selectByPrimaryKey(Long cartid);

    int updateByPrimaryKeySelective(PMyproductext record);

    int updateByPrimaryKey(PMyproductext record);
}