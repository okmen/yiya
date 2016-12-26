package com.bbyiya.dao;

import com.bbyiya.model.PProductdetails;

public interface PProductdetailsMapper {
    int deleteByPrimaryKey(Long detailid);

    int insert(PProductdetails record);

    int insertSelective(PProductdetails record);

    PProductdetails selectByPrimaryKey(Long detailid);

    int updateByPrimaryKeySelective(PProductdetails record);

    int updateByPrimaryKey(PProductdetails record);
}