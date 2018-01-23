package com.bbyiya.dao;

import com.bbyiya.model.UUsertesterwx;

public interface UUsertesterwxMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UUsertesterwx record);

    int insertSelective(UUsertesterwx record);

    UUsertesterwx selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UUsertesterwx record);

    int updateByPrimaryKey(UUsertesterwx record);
    /**
     * 获取12pic的测试人数
     * @return
     */
    Integer getMaxSort();
}