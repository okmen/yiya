package com.bbyiya.dao;

import com.bbyiya.model.TiGroupactivityproducts;

public interface TiGroupactivityproductsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiGroupactivityproducts record);

    int insertSelective(TiGroupactivityproducts record);

    TiGroupactivityproducts selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiGroupactivityproducts record);

    int updateByPrimaryKey(TiGroupactivityproducts record);
}