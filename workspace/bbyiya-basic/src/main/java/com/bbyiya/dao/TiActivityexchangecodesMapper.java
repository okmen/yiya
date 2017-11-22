package com.bbyiya.dao;

import com.bbyiya.model.TiActivityexchangecodes;

public interface TiActivityexchangecodesMapper {
    int deleteByPrimaryKey(String codenum);

    int insert(TiActivityexchangecodes record);

    int insertSelective(TiActivityexchangecodes record);

    TiActivityexchangecodes selectByPrimaryKey(String codenum);

    int updateByPrimaryKeySelective(TiActivityexchangecodes record);

    int updateByPrimaryKey(TiActivityexchangecodes record);
}