package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiGroupactivityworks;

public interface TiGroupactivityworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiGroupactivityworks record);

    int insertSelective(TiGroupactivityworks record);

    TiGroupactivityworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiGroupactivityworks record);

    int updateByPrimaryKey(TiGroupactivityworks record);
    /**
     * 获取用户参与 某团购的信息
     * @param userId
     * @param gactId 团购Id
     * @return
     */
    TiGroupactivityworks getTiGroupactivityworksByActIdAndUserId(@Param("userId")Long userId,@Param("gactId")Integer gactId);
}