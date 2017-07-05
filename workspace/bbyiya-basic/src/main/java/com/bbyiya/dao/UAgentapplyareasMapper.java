package com.bbyiya.dao;

import com.bbyiya.model.UAgentapplyareas;

public interface UAgentapplyareasMapper {
    int deleteByPrimaryKey(Long acodeid);

    int insert(UAgentapplyareas record);

    int insertSelective(UAgentapplyareas record);

    UAgentapplyareas selectByPrimaryKey(Long acodeid);

    int updateByPrimaryKeySelective(UAgentapplyareas record);

    int updateByPrimaryKey(UAgentapplyareas record);
}