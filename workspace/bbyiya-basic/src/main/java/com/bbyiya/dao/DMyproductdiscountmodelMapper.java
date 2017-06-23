package com.bbyiya.dao;

import com.bbyiya.model.DMyproductdiscountmodel;

public interface DMyproductdiscountmodelMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(DMyproductdiscountmodel record);

    int insertSelective(DMyproductdiscountmodel record);

    DMyproductdiscountmodel selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(DMyproductdiscountmodel record);

    int updateByPrimaryKey(DMyproductdiscountmodel record);
}