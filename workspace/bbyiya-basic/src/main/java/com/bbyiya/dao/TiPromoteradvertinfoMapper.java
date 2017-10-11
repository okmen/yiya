package com.bbyiya.dao;

import com.bbyiya.model.TiPromoteradvertinfo;

public interface TiPromoteradvertinfoMapper {
    int deleteByPrimaryKey(Integer advertid);

    int insert(TiPromoteradvertinfo record);

    int insertSelective(TiPromoteradvertinfo record);

    TiPromoteradvertinfo selectByPrimaryKey(Integer advertid);

    int updateByPrimaryKeySelective(TiPromoteradvertinfo record);

    int updateByPrimaryKey(TiPromoteradvertinfo record);
}