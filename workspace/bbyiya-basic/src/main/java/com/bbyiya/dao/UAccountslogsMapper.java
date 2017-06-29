package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UAccountslogs;

public interface UAccountslogsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(UAccountslogs record);

    int insertSelective(UAccountslogs record);

    UAccountslogs selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(UAccountslogs record);

    int updateByPrimaryKey(UAccountslogs record);
    /**
     * 用户账户流水
     * @param userId
     * @param type
     * @return
     */
    List<UAccountslogs> findAccountsLogs(@Param("userId")Long userId,@Param("accountLogType")Integer type);
}