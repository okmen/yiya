package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PStylecoordinate;

public interface PStylecoordinateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PStylecoordinate record);

    int insertSelective(PStylecoordinate record);

    PStylecoordinate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PStylecoordinate record);

    int updateByPrimaryKey(PStylecoordinate record);
    
    /**
     * 获取列表
     * @param styleId
     * @return
     */
    List<PStylecoordinate> findlistByStyleId(@Param("styleId")Long styleId);
}