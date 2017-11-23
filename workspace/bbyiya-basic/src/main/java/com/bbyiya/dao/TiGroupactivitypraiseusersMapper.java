package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiGroupactivitypraiseusers;

public interface TiGroupactivitypraiseusersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiGroupactivitypraiseusers record);

    int insertSelective(TiGroupactivitypraiseusers record);

    TiGroupactivitypraiseusers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiGroupactivitypraiseusers record);

    int updateByPrimaryKey(TiGroupactivitypraiseusers record);
    /**
     * 作品点赞列表
     * @param workId
     * @return
     */
    List<TiGroupactivitypraiseusers>findlistByWorkId(@Param("workId")Long workId);
    /**
     * 确认是否已经点过赞
     * @param workId
     * @param userId
     * @return
     */
    TiGroupactivitypraiseusers existsByWorkIdAndUserId(@Param("workId")Long workId,@Param("userId")Long userId);
}