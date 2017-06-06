package com.bbyiya.dao;

import com.bbyiya.model.OOrderproductphotos;

public interface OOrderproductphotosMapper {
    int deleteByPrimaryKey(Long odetailid);

    int insert(OOrderproductphotos record);

    int insertSelective(OOrderproductphotos record);

    OOrderproductphotos selectByPrimaryKey(Long odetailid);

    int updateByPrimaryKeySelective(OOrderproductphotos record);

    int updateByPrimaryKey(OOrderproductphotos record);
}