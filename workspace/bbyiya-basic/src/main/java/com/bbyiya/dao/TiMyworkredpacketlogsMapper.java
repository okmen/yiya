package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiMyworkredpacketlogs;

public interface TiMyworkredpacketlogsMapper {
    int deleteByPrimaryKey(String payid);

    int insert(TiMyworkredpacketlogs record);

    int insertSelective(TiMyworkredpacketlogs record);

    TiMyworkredpacketlogs selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(TiMyworkredpacketlogs record);

    int updateByPrimaryKey(TiMyworkredpacketlogs record);
    /**
     * 个人发红包的记录
     * @param userId
     * @param status
     * @return
     */
    List<TiMyworkredpacketlogs> findredpacketLogs(@Param("userId")Long userId,@Param("workId")Long workId,@Param("status")Integer status);
}