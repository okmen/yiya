package com.bbyiya.dao;

import com.bbyiya.model.SysLogs;

public interface SysLogsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(SysLogs record);

    int insertSelective(SysLogs record);

    SysLogs selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(SysLogs record);

    int updateByPrimaryKeyWithBLOBs(SysLogs record);

    int updateByPrimaryKey(SysLogs record);
}