package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PStylebackgrounds;

public interface PStylebackgroundsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PStylebackgrounds record);

    int insertSelective(PStylebackgrounds record);

    PStylebackgrounds selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PStylebackgrounds record);

    int updateByPrimaryKey(PStylebackgrounds record);
    /**
     * 获取款式背景图列表
     * @param styleId
     * @return
     */
    List<PStylebackgrounds> findBacksByStyleId(@Param("styleId") Long styleId); 
}