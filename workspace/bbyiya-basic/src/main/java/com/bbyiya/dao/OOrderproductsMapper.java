package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OOrderproducts;

public interface OOrderproductsMapper {
    int deleteByPrimaryKey(String orderproductid);

    int insert(OOrderproducts record);

    int insertSelective(OOrderproducts record);

    OOrderproducts selectByPrimaryKey(String orderproductid);

    int updateByPrimaryKeySelective(OOrderproducts record);

    int updateByPrimaryKey(OOrderproducts record);
    /**
     * 获取订单产品列表
     * @param userOrderId
     * @return
     */
    List<OOrderproducts>  findOProductsByOrderId(@Param("userOrderId")String userOrderId);
}