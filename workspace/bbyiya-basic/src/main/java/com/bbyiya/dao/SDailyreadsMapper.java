package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.SDailyreads;

public interface SDailyreadsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SDailyreads record);

    int insertSelective(SDailyreads record);

    SDailyreads selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SDailyreads record);

    int updateByPrimaryKeyWithBLOBs(SDailyreads record);

    int updateByPrimaryKey(SDailyreads record);
    /**
     * 获取每日读物
     * @param forday
     * @return
     */
    List<SDailyreads> findDailyReadslist(@Param("forDay") Integer forday);
}