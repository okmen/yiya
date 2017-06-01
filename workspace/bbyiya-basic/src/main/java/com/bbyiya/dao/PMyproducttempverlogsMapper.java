package com.bbyiya.dao;

import com.bbyiya.model.PMyproducttempverlogs;

public interface PMyproducttempverlogsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(PMyproducttempverlogs record);

    int insertSelective(PMyproducttempverlogs record);

    PMyproducttempverlogs selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(PMyproducttempverlogs record);

    int updateByPrimaryKey(PMyproducttempverlogs record);
}