package com.bbyiya.dao;

import com.bbyiya.model.OPayorderwalletdetails;

public interface OPayorderwalletdetailsMapper {
    int deleteByPrimaryKey(String payid);

    int insert(OPayorderwalletdetails record);

    int insertSelective(OPayorderwalletdetails record);

    OPayorderwalletdetails selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(OPayorderwalletdetails record);

    int updateByPrimaryKey(OPayorderwalletdetails record);
}