package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiActivitys;
import com.bbyiya.vo.calendar.TiActivitysVo;

public interface TiActivitysMapper {
    int deleteByPrimaryKey(Integer actid);

    int insert(TiActivitys record);

    int insertSelective(TiActivitys record);

    TiActivitys selectByPrimaryKey(Integer actid);

    int updateByPrimaryKeySelective(TiActivitys record);

    int updateByPrimaryKeyWithBLOBs(TiActivitys record);

    int updateByPrimaryKey(TiActivitys record);
    
    List<TiActivitysVo> findCalenderActivityList(@Param("produceruserid") Long produceruserid,@Param("status") Integer status,@Param("keywords") String keywords,@Param("type")Integer type);
}