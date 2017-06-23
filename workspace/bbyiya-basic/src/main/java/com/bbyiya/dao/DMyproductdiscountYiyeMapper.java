package com.bbyiya.dao;

import com.bbyiya.model.DMyproductdiscountYiye;

public interface DMyproductdiscountYiyeMapper {
    int deleteByPrimaryKey(Long cartid);

    int insert(DMyproductdiscountYiye record);

    int insertSelective(DMyproductdiscountYiye record);

    DMyproductdiscountYiye selectByPrimaryKey(Long cartid);

    int updateByPrimaryKeySelective(DMyproductdiscountYiye record);

    int updateByPrimaryKey(DMyproductdiscountYiye record);
}