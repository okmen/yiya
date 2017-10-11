package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiPromoteradvertinfo;

public interface TiPromoteradvertinfoMapper {
    int deleteByPrimaryKey(Integer advertid);

    int insert(TiPromoteradvertinfo record);

    int insertSelective(TiPromoteradvertinfo record);

    TiPromoteradvertinfo selectByPrimaryKey(Integer advertid);

    int updateByPrimaryKeySelective(TiPromoteradvertinfo record);

    int updateByPrimaryKey(TiPromoteradvertinfo record);
    /**
     * 获取 广告信息
     * @param userId
     * @return
     */
    TiPromoteradvertinfo getModelByPromoterUserId(@Param("userId")Long userId);
}