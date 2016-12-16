package com.bbyiya.dao;

import com.bbyiya.model.SDailyreads;

public interface SDailyreadsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SDailyreads record);

    int insertSelective(SDailyreads record);

    SDailyreads selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SDailyreads record);

    int updateByPrimaryKeyWithBLOBs(SDailyreads record);

    int updateByPrimaryKey(SDailyreads record);
}