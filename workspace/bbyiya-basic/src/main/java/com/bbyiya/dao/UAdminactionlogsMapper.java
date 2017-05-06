package com.bbyiya.dao;

import com.bbyiya.model.UAdminactionlogs;

public interface UAdminactionlogsMapper {
	
    int deleteByPrimaryKey(Long logid);

    int insert(UAdminactionlogs record);

    int insertSelective(UAdminactionlogs record);

    UAdminactionlogs selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(UAdminactionlogs record);

    int updateByPrimaryKey(UAdminactionlogs record);
}