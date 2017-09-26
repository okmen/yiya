package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiAccountlog;

public interface TiAccountlogMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(TiAccountlog record);

    int insertSelective(TiAccountlog record);

    TiAccountlog selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(TiAccountlog record);

    int updateByPrimaryKey(TiAccountlog record);
    
    /**
     * 获取最新一条交易信息（获取总交易额，总余额）
     * @return
     */
    TiAccountlog getLastResult();
    /**
     * 查询交易流水
     * @return
     */
    List<TiAccountlog> findList();
}