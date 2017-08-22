package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.UAgents;

public interface UAgentsMapper {
    int deleteByPrimaryKey(Long agentuserid);

    int insert(UAgents record);

    int insertSelective(UAgents record);

    UAgents selectByPrimaryKey(Long agentuserid);

    int updateByPrimaryKeySelective(UAgents record);

    int updateByPrimaryKey(UAgents record);
    
    List<UAgents> findAgentlistAll();
}