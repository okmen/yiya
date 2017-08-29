package com.bbyiya.dao;

import com.bbyiya.model.TiAgents;

public interface TiAgentsMapper {
    int deleteByPrimaryKey(Long agentuserid);

    int insert(TiAgents record);

    int insertSelective(TiAgents record);

    TiAgents selectByPrimaryKey(Long agentuserid);

    int updateByPrimaryKeySelective(TiAgents record);

    int updateByPrimaryKey(TiAgents record);
}