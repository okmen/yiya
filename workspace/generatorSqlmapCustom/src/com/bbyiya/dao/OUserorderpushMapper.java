package com.bbyiya.dao;

import com.bbyiya.model.OUserorderpush;

public interface OUserorderpushMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OUserorderpush record);

    int insertSelective(OUserorderpush record);

    OUserorderpush selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OUserorderpush record);

    int updateByPrimaryKey(OUserorderpush record);
}