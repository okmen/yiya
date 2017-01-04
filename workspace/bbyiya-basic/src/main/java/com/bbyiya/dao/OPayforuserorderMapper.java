package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OPayforuserorder;

public interface OPayforuserorderMapper {
    int deleteByPrimaryKey(String payid);

    int insert(OPayforuserorder record);

    int insertSelective(OPayforuserorder record);

    OPayforuserorder selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(OPayforuserorder record);

    int updateByPrimaryKey(OPayforuserorder record);
    
    /**
     * 获取支付Id
     * @param userOrderId
     * @return
     */
    OPayforuserorder getPayOrderByUserOrderId(@Param("userOrderId") String userOrderId);
}