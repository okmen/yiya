package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UAgentapplyareas;

public interface UAgentapplyareasMapper {
    int deleteByPrimaryKey(Long acodeid);

    int insert(UAgentapplyareas record);

    int insertSelective(UAgentapplyareas record);

    UAgentapplyareas selectByPrimaryKey(Long acodeid);

    int updateByPrimaryKeySelective(UAgentapplyareas record);

    int updateByPrimaryKey(UAgentapplyareas record);
    
    List<UAgentapplyareas> findAgentapplyareasByUserId(@Param("userid") Long userid);
}