package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiMachineproducts;

public interface TiMachineproductsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiMachineproducts record);

    int insertSelective(TiMachineproducts record);

    TiMachineproducts selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiMachineproducts record);

    int updateByPrimaryKey(TiMachineproducts record);
    
    List<TiMachineproducts>findProductListByMachineId(@Param("machineid") Integer machineid);
}