package com.bbyiya.dao;

import com.bbyiya.model.PProductstyleexp;

public interface PProductstyleexpMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(PProductstyleexp record);

    int insertSelective(PProductstyleexp record);

    PProductstyleexp selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(PProductstyleexp record);

    int updateByPrimaryKey(PProductstyleexp record);
}