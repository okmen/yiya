package com.bbyiya.dao;

import com.bbyiya.model.PScenebacks;

public interface PScenebacksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PScenebacks record);

    int insertSelective(PScenebacks record);

    PScenebacks selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PScenebacks record);

    int updateByPrimaryKey(PScenebacks record);
}