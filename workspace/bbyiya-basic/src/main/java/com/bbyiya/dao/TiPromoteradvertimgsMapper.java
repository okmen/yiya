package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiPromoteradvertimgs;

public interface TiPromoteradvertimgsMapper {
	
    int deleteByPrimaryKey(Long id);

    int insert(TiPromoteradvertimgs record);

    int insertSelective(TiPromoteradvertimgs record);

    TiPromoteradvertimgs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiPromoteradvertimgs record);

    int updateByPrimaryKey(TiPromoteradvertimgs record);
    /**
     * 获取广告图集
     * @param advertId
     * @return
     */
    List<TiPromoteradvertimgs> findImgsByAdvertId(@Param("advertId")Integer advertId);
}