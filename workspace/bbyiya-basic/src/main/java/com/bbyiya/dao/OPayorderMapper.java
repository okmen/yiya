package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OPayorder;

public interface OPayorderMapper {
    int deleteByPrimaryKey(String payid);

    int insert(OPayorder record);

    int insertSelective(OPayorder record);

    OPayorder selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(OPayorder record);

    int updateByPrimaryKey(OPayorder record);
    /**
     * 根据作品workId获取邮费订单
     * @param workId
     * @return
     */
    OPayorder getpostPayorderByworkId(@Param("workId")String workId);
}