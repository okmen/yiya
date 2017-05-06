package com.bbyiya.dao;

import com.bbyiya.model.OExcludedareas;

public interface OExcludedareasMapper {
    int deleteByPrimaryKey(Integer areacode);

    int insert(OExcludedareas record);

    int insertSelective(OExcludedareas record);

    OExcludedareas selectByPrimaryKey(Integer areacode);

    int updateByPrimaryKeySelective(OExcludedareas record);

    int updateByPrimaryKey(OExcludedareas record);
}