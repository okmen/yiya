package com.bbyiya.dao;

import com.bbyiya.model.TiMachineproducts;

public interface TiMachineproductsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiMachineproducts record);

    int insertSelective(TiMachineproducts record);

    TiMachineproducts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiMachineproducts record);

    int updateByPrimaryKey(TiMachineproducts record);
}