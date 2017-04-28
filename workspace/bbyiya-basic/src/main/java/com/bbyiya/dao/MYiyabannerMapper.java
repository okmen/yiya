package com.bbyiya.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.MYiyabanner;

public interface MYiyabannerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MYiyabanner record);

    int insertSelective(MYiyabanner record);

    MYiyabanner selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MYiyabanner record);

    int updateByPrimaryKey(MYiyabanner record);
    /**
     * 根据当前时间获取咿呀说banner
     * @param currentTime
     * @return
     */
    List<MYiyabanner> find_MYiyabannerByCurrentTime(@Param("curTime") Date currentTime);
}