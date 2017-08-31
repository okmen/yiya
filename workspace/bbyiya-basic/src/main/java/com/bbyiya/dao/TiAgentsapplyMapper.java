package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiAgentsapply;
import com.bbyiya.vo.calendar.TiAgentApplyVo;
import com.bbyiya.vo.calendar.TiAgentSearchParam;

public interface TiAgentsapplyMapper {
    int deleteByPrimaryKey(Long agentuserid);

    int insert(TiAgentsapply record);

    int insertSelective(TiAgentsapply record);

    TiAgentsapply selectByPrimaryKey(Long agentuserid);

    int updateByPrimaryKeySelective(TiAgentsapply record);

    int updateByPrimaryKey(TiAgentsapply record);
    
    TiAgentApplyVo getUAgentapplyVOByAgentUserId(Long agentuserid);
    
    List<TiAgentApplyVo> findTiAgentapplyVOList(TiAgentSearchParam param);
}