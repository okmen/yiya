package com.bbyiya.dao;

import com.bbyiya.model.UOtherlogin;

public interface UOtherloginMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UOtherlogin record);

    int insertSelective(UOtherlogin record);

    UOtherlogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UOtherlogin record);

    int updateByPrimaryKey(UOtherlogin record);
}