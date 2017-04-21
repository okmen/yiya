package com.bbyiya.dao;

import com.bbyiya.model.UWeiusers;

public interface UWeiusersMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UWeiusers record);

    int insertSelective(UWeiusers record);

    UWeiusers selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UWeiusers record);

    int updateByPrimaryKey(UWeiusers record);
}