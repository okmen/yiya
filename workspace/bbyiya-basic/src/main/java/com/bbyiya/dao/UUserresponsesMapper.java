package com.bbyiya.dao;

import com.bbyiya.model.UUserresponses;

public interface UUserresponsesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UUserresponses record);

    int insertSelective(UUserresponses record);

    UUserresponses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UUserresponses record);

    int updateByPrimaryKey(UUserresponses record);
}