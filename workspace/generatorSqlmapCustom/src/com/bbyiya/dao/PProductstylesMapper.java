package com.bbyiya.dao;

import com.bbyiya.model.PProductstyles;

public interface PProductstylesMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(PProductstyles record);

    int insertSelective(PProductstyles record);

    PProductstyles selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(PProductstyles record);

    int updateByPrimaryKey(PProductstyles record);
}