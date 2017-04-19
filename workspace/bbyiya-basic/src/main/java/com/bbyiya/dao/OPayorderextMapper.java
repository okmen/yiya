package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OPayorderext;

public interface OPayorderextMapper {
    int deleteByPrimaryKey(String payid);

    int insert(OPayorderext record);

    int insertSelective(OPayorderext record);

    OPayorderext selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(OPayorderext record);

    int updateByPrimaryKey(OPayorderext record);
    /**
     * 根据推荐人获取订单列表
     * @param upuserid
     * @return
     */
    List<OPayorderext> findListByUpUserid(@Param("upuserid") Long upuserid);
}