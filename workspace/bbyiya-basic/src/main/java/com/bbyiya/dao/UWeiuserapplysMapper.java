package com.bbyiya.dao;

import com.bbyiya.model.UWeiuserapplys;

public interface UWeiuserapplysMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UWeiuserapplys record);

    int insertSelective(UWeiuserapplys record);

    UWeiuserapplys selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UWeiuserapplys record);

    int updateByPrimaryKey(UWeiuserapplys record);
}