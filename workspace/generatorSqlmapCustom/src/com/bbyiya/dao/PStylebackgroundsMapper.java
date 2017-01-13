package com.bbyiya.dao;

import com.bbyiya.model.PStylebackgrounds;

public interface PStylebackgroundsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PStylebackgrounds record);

    int insertSelective(PStylebackgrounds record);

    PStylebackgrounds selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PStylebackgrounds record);

    int updateByPrimaryKey(PStylebackgrounds record);
}