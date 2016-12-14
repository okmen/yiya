package com.bbyiya.dao;

import com.bbyiya.model.UAdmin;

public interface UAdminMapper {
    int deleteByPrimaryKey(Integer adminid);

    int insert(UAdmin record);

    int insertSelective(UAdmin record);

    UAdmin selectByPrimaryKey(Integer adminid);

    int updateByPrimaryKeySelective(UAdmin record);

    int updateByPrimaryKey(UAdmin record);
}