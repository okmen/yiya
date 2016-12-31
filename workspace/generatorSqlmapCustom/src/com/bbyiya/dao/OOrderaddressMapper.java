package com.bbyiya.dao;

import com.bbyiya.model.OOrderaddress;

public interface OOrderaddressMapper {
    int deleteByPrimaryKey(Long orderaddressid);

    int insert(OOrderaddress record);

    int insertSelective(OOrderaddress record);

    OOrderaddress selectByPrimaryKey(Long orderaddressid);

    int updateByPrimaryKeySelective(OOrderaddress record);

    int updateByPrimaryKey(OOrderaddress record);
}