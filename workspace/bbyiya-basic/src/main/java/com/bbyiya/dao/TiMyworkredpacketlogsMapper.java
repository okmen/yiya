package com.bbyiya.dao;

import com.bbyiya.model.TiMyworkredpacketlogs;

public interface TiMyworkredpacketlogsMapper {
    int deleteByPrimaryKey(String payid);

    int insert(TiMyworkredpacketlogs record);

    int insertSelective(TiMyworkredpacketlogs record);

    TiMyworkredpacketlogs selectByPrimaryKey(String payid);

    int updateByPrimaryKeySelective(TiMyworkredpacketlogs record);

    int updateByPrimaryKey(TiMyworkredpacketlogs record);
}