package com.bbyiya.dao;

import com.bbyiya.model.PMyproducttemp;

public interface PMyproducttempMapper {
    int deleteByPrimaryKey(Integer tempid);

    int insert(PMyproducttemp record);

    int insertSelective(PMyproducttemp record);

    PMyproducttemp selectByPrimaryKey(Integer tempid);

    int updateByPrimaryKeySelective(PMyproducttemp record);

    int updateByPrimaryKey(PMyproducttemp record);
}