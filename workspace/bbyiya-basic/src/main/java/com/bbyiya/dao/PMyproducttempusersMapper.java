package com.bbyiya.dao;

import com.bbyiya.model.PMyproducttempusers;

public interface PMyproducttempusersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PMyproducttempusers record);

    int insertSelective(PMyproducttempusers record);

    PMyproducttempusers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PMyproducttempusers record);

    int updateByPrimaryKey(PMyproducttempusers record);
}