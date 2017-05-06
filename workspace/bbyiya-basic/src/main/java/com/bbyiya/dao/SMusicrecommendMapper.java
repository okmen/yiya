package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.SMusicrecommend;

public interface SMusicrecommendMapper {
	
    int deleteByPrimaryKey(Integer reid);

    int insert(SMusicrecommend record);

    int insertSelective(SMusicrecommend record);

    SMusicrecommend selectByPrimaryKey(Integer reid);

    int updateByPrimaryKeySelective(SMusicrecommend record);

    int updateByPrimaryKey(SMusicrecommend record);
    /**
     * 根据时间获取每日推荐音乐
     * @param day 第n天
     * @return
     */
    List<SMusicrecommend> findSMusicrecommendByDay(int day);
}