package com.bbyiya.dao;

import com.bbyiya.model.PPostmodel;

public interface PPostmodelMapper {
    int deleteByPrimaryKey(Integer postmodelid);

    int insert(PPostmodel record);

    int insertSelective(PPostmodel record);

    PPostmodel selectByPrimaryKey(Integer postmodelid);

    int updateByPrimaryKeySelective(PPostmodel record);

    int updateByPrimaryKey(PPostmodel record);
}