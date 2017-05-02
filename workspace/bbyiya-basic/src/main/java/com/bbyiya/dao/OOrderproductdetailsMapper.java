package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OOrderproductdetails;

public interface OOrderproductdetailsMapper {
    int deleteByPrimaryKey(Long oproductdetailid);

    int insert(OOrderproductdetails record);

    int insertSelective(OOrderproductdetails record);

    OOrderproductdetails selectByPrimaryKey(Long oproductdetailid);

    int updateByPrimaryKeySelective(OOrderproductdetails record);

    int updateByPrimaryKey(OOrderproductdetails record);
    
    /**
     * 查询订单图片
     * @param orderproductId
     * @return
     */
    List<OOrderproductdetails> findOrderProductDetailsByProductOrderId(@Param("orderProductId") String orderproductId);
}