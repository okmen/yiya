package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OUserorders;

public interface OUserordersMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OUserorders record);

    int insertSelective(OUserorders record);

    OUserorders selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OUserorders record);

    int updateByPrimaryKey(OUserorders record);
    /**
     * 获取用户订单列表
     * @param userId
     * @return
     */
    List<OUserorders> findOrderByUserId(@Param("userId") Long userId);
}