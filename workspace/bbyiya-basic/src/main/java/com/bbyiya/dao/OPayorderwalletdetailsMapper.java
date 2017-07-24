package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OPayorderwalletdetails;

public interface OPayorderwalletdetailsMapper {
    int deleteByPrimaryKey(String payid);

    int insert(OPayorderwalletdetails record);

    int insertSelective(OPayorderwalletdetails record);

    OPayorderwalletdetails selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(OPayorderwalletdetails record);

    int updateByPrimaryKey(OPayorderwalletdetails record);
    
    List<OPayorderwalletdetails> findWalletsByCartId(@Param("cartId")Long cartId);
}