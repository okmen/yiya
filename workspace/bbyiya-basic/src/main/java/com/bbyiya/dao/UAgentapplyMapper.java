package com.bbyiya.dao;

import com.bbyiya.model.UAgentapply;

public interface UAgentapplyMapper {
    int deleteByPrimaryKey(Long agentuserid);

    int insert(UAgentapply record);

    int insertSelective(UAgentapply record);

    UAgentapply selectByPrimaryKey(Long agentuserid);

    int updateByPrimaryKeySelective(UAgentapply record);

    int updateByPrimaryKey(UAgentapply record);
}