package com.bbyiya.dao;

import com.bbyiya.model.DMyproductdiscounts;

public interface DMyproductdiscountsMapper {
    int deleteByPrimaryKey(Long cartid);

    int insert(DMyproductdiscounts record);

    int insertSelective(DMyproductdiscounts record);

    DMyproductdiscounts selectByPrimaryKey(Long cartid);

    int updateByPrimaryKeySelective(DMyproductdiscounts record);

    int updateByPrimaryKey(DMyproductdiscounts record);
}