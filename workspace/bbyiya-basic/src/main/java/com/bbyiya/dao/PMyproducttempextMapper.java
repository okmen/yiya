package com.bbyiya.dao;

import com.bbyiya.model.PMyproducttempext;

public interface PMyproducttempextMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PMyproducttempext record);

    int insertSelective(PMyproducttempext record);

    PMyproducttempext selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PMyproducttempext record);

    int updateByPrimaryKey(PMyproducttempext record);
}