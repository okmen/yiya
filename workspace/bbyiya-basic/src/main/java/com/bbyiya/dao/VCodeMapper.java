package com.bbyiya.dao;

import com.bbyiya.model.VCode;

public interface VCodeMapper {
    int deleteByPrimaryKey(Long codeid);

    int insert(VCode record);

    int insertSelective(VCode record);

    VCode selectByPrimaryKey(Long codeid);

    int updateByPrimaryKeySelective(VCode record);

    int updateByPrimaryKey(VCode record);
}