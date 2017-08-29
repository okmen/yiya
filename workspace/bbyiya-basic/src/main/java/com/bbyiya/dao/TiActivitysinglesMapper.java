package com.bbyiya.dao;

import com.bbyiya.model.TiActivitysingles;

public interface TiActivitysinglesMapper {
    int deleteByPrimaryKey(Long actsingleid);

    int insert(TiActivitysingles record);

    int insertSelective(TiActivitysingles record);

    TiActivitysingles selectByPrimaryKey(Long actsingleid);

    int updateByPrimaryKeySelective(TiActivitysingles record);

    int updateByPrimaryKey(TiActivitysingles record);
}