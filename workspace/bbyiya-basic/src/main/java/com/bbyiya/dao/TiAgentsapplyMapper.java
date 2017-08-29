package com.bbyiya.dao;

import com.bbyiya.model.TiAgentsapply;

public interface TiAgentsapplyMapper {
    int deleteByPrimaryKey(Long agentuserid);

    int insert(TiAgentsapply record);

    int insertSelective(TiAgentsapply record);

    TiAgentsapply selectByPrimaryKey(Long agentuserid);

    int updateByPrimaryKeySelective(TiAgentsapply record);

    int updateByPrimaryKey(TiAgentsapply record);
}