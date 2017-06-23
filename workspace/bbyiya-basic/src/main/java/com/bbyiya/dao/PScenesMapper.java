package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PScenes;

public interface PScenesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PScenes record);

    int insertSelective(PScenes record);

    PScenes selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PScenes record);

    int updateByPrimaryKey(PScenes record);
    /**
     * 获取场景列表
     * @param productId
     * @return
     */
    List<PScenes> findScenesByProductId(@Param("productId")Long productId);
    
    /**
     * 获取场景列表
     * @param productId
     * @return
     */
    List<PScenes> findScenesByProductIdOrkeyword(@Param("productId")Long productId,@Param("keywords")String keywords);
}