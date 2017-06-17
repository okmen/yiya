package com.bbyiya.dao;

import com.bbyiya.model.PMyproductactivitycode;

public interface PMyproductactivitycodeMapper {
    int deleteByPrimaryKey(String codeno);

    int insert(PMyproductactivitycode record);

    int insertSelective(PMyproductactivitycode record);

    PMyproductactivitycode selectByPrimaryKey(String codeno);

    int updateByPrimaryKeySelective(PMyproductactivitycode record);

    int updateByPrimaryKey(PMyproductactivitycode record);
}