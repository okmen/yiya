package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.SMusicttype;

public interface SMusicttypeMapper {
    int deleteByPrimaryKey(Integer musictypeid);

    int insert(SMusicttype record);

    int insertSelective(SMusicttype record);

    SMusicttype selectByPrimaryKey(Integer musictypeid);

    int updateByPrimaryKeySelective(SMusicttype record);

    int updateByPrimaryKey(SMusicttype record);
    
    /**
     * 获取音乐类型列表
     * @return
     */
    List<SMusicttype> findMusictTypeAll();
}