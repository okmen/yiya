package com.bbyiya.dao;

import com.bbyiya.model.TiProducerapplymachines;

public interface TiProducerapplymachinesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiProducerapplymachines record);

    int insertSelective(TiProducerapplymachines record);

    TiProducerapplymachines selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiProducerapplymachines record);

    int updateByPrimaryKey(TiProducerapplymachines record);
}