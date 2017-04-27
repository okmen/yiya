package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.SysMessage;

public interface SysMessageMapper {
    int deleteByPrimaryKey(Integer msgid);

    int insert(SysMessage record);

    int insertSelective(SysMessage record);

    SysMessage selectByPrimaryKey(Integer msgid);

    int updateByPrimaryKeySelective(SysMessage record);

    int updateByPrimaryKeyWithBLOBs(SysMessage record);

    int updateByPrimaryKey(SysMessage record);

    List<SysMessage> selectSysMessage(@Param("startTimeStr") String startTimeStr,@Param("endTimeStr") String endTimeStr);
}