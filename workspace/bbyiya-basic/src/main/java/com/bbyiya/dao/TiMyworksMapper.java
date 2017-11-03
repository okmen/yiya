package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiMyworks;

public interface TiMyworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiMyworks record);

    int insertSelective(TiMyworks record);

    TiMyworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiMyworks record);

    int updateByPrimaryKey(TiMyworks record);
    
    List<TiMyworks>selectDirtyDataByUserId(@Param("userid") Long userid);

}