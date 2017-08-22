package com.bbyiya.dao;

import com.bbyiya.model.PAgentmyproducttempview;

public interface PAgentmyproducttempviewMapper {
    int deleteByPrimaryKey(Long agentuserid);

    int insert(PAgentmyproducttempview record);

    int insertSelective(PAgentmyproducttempview record);

    PAgentmyproducttempview selectByPrimaryKey(Long agentuserid);

    int updateByPrimaryKeySelective(PAgentmyproducttempview record);

    int updateByPrimaryKey(PAgentmyproducttempview record);
}