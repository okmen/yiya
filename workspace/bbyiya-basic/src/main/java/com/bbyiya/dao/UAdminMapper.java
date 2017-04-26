package com.bbyiya.dao;

import com.bbyiya.model.UAdmin;

public interface UAdminMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UAdmin record);

    int insertSelective(UAdmin record);

    UAdmin selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UAdmin record);

    int updateByPrimaryKey(UAdmin record);
}