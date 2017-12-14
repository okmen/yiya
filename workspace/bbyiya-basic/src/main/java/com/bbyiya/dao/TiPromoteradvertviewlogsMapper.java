package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiPromoteradvertviewlogs;

public interface TiPromoteradvertviewlogsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(TiPromoteradvertviewlogs record);

    int insertSelective(TiPromoteradvertviewlogs record);

    TiPromoteradvertviewlogs selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(TiPromoteradvertviewlogs record);

    int updateByPrimaryKey(TiPromoteradvertviewlogs record);
    /**
     * 根据advertId,userId找到用户记录
     * @param userId
     * @param advertId
     * @return
     */
    TiPromoteradvertviewlogs getByAdIdAndUid(@Param("userId")Long userId,@Param("adverId")Integer advertId);
    /**
     * 获取广告的浏览记录
     * @param advertId
     * @return
     */
    List<TiPromoteradvertviewlogs> findlistByAdvertId(@Param("advertId") Integer advertId);
}