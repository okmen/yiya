package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.RAreaplans;

public interface RAreaplansMapper {
    int deleteByPrimaryKey(Integer areacode);

    int insert(RAreaplans record);

    int insertSelective(RAreaplans record);

    RAreaplans selectByPrimaryKey(Integer areacode);

    int updateByPrimaryKeySelective(RAreaplans record);

    int updateByPrimaryKey(RAreaplans record);
    
    RAreaplans getApplyedAreacodeBycode(@Param("userid") Long userid,@Param("areacode") Integer areacode);
}