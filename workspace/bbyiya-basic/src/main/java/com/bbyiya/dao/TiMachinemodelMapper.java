package com.bbyiya.dao;

import com.bbyiya.model.TiMachinemodel;

public interface TiMachinemodelMapper {
    int deleteByPrimaryKey(Integer machineid);

    int insert(TiMachinemodel record);

    int insertSelective(TiMachinemodel record);

    TiMachinemodel selectByPrimaryKey(Integer machineid);

    int updateByPrimaryKeySelective(TiMachinemodel record);

    int updateByPrimaryKey(TiMachinemodel record);
}