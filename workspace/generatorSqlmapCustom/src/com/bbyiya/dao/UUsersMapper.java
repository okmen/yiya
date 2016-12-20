package com.bbyiya.dao;

import com.bbyiya.model.UUsers;

public interface UUsersMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UUsers record);

    int insertSelective(UUsers record);

    UUsers selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UUsers record);

    int updateByPrimaryKey(UUsers record);
}