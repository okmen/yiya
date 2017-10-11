package com.bbyiya.dao;

import com.bbyiya.model.TiPromoteradvertcoustomer;

public interface TiPromoteradvertcoustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiPromoteradvertcoustomer record);

    int insertSelective(TiPromoteradvertcoustomer record);

    TiPromoteradvertcoustomer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiPromoteradvertcoustomer record);

    int updateByPrimaryKey(TiPromoteradvertcoustomer record);
}