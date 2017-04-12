package com.bbyiya.dao;

import com.bbyiya.model.SysMessage;

public interface SysMessageMapper {
    int deleteByPrimaryKey(Integer msgid);

    int insert(SysMessage record);

    int insertSelective(SysMessage record);

    SysMessage selectByPrimaryKey(Integer msgid);

    int updateByPrimaryKeySelective(SysMessage record);

    int updateByPrimaryKeyWithBLOBs(SysMessage record);

    int updateByPrimaryKey(SysMessage record);
}