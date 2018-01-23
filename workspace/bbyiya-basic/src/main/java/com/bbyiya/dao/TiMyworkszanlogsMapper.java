package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiMyworkszanlogs;

public interface TiMyworkszanlogsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(TiMyworkszanlogs record);

    int insertSelective(TiMyworkszanlogs record);

    TiMyworkszanlogs selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(TiMyworkszanlogs record);

    int updateByPrimaryKey(TiMyworkszanlogs record);
    /**
     * 获取作品点赞列表
     * @param workId
     * @return
     */
    List<TiMyworkszanlogs> findListByWorkId(@Param("workId")Long workId);
    /**
     * 是否点过赞
     * @param workId
     * @param userId
     * @return
     */
    int countByWorkIdAndUserId(@Param("workId")Long workId,@Param("userId")Long userId);
}