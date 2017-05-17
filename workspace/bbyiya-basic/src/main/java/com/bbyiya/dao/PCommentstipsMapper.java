package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PCommentstips;

public interface PCommentstipsMapper {
    int deleteByPrimaryKey(Integer tipid);

    int insert(PCommentstips record);

    int insertSelective(PCommentstips record);

    PCommentstips selectByPrimaryKey(Integer tipid);

    int updateByPrimaryKeySelective(PCommentstips record);

    int updateByPrimaryKey(PCommentstips record);
    /**
     * 根据评论类型获取评论提示列表
     * @param tipclassid
     * @return
     */
    List<PCommentstips> findListByTempId(@Param("tipClassId")Integer tipclassid);
}