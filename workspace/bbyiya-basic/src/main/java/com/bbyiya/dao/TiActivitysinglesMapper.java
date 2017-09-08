package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiActivitysingles;

public interface TiActivitysinglesMapper {
    int deleteByPrimaryKey(Long actsingleid);

    int insert(TiActivitysingles record);
    /**
     * 新增
     * @param record
     * @return
     */
    int insertReturnId(TiActivitysingles record);
    int insertSelective(TiActivitysingles record);

    TiActivitysingles selectByPrimaryKey(Long actsingleid);

    int updateByPrimaryKeySelective(TiActivitysingles record);

    int updateByPrimaryKey(TiActivitysingles record);
    
    Integer getYaoqingCountByActId(@Param("actid") Integer actid);
}