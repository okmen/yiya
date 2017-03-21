package com.bbyiya.dao;

import com.bbyiya.model.OOrderproductdetails;

public interface OOrderproductdetailsMapper {
    int deleteByPrimaryKey(Long oproductdetailid);

    int insert(OOrderproductdetails record);

    int insertSelective(OOrderproductdetails record);

    OOrderproductdetails selectByPrimaryKey(Long oproductdetailid);

    int updateByPrimaryKeySelective(OOrderproductdetails record);

    int updateByPrimaryKey(OOrderproductdetails record);
}