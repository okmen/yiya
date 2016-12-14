package com.bbyiya.dao;

import com.bbyiya.model.UAdminloginlog;

public interface UAdminloginlogMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(UAdminloginlog record);

    int insertSelective(UAdminloginlog record);

    UAdminloginlog selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(UAdminloginlog record);

    int updateByPrimaryKey(UAdminloginlog record);
}