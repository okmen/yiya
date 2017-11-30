package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiProductshowtemplateinfo;

public interface TiProductshowtemplateinfoMapper {
    int deleteByPrimaryKey(Integer tempinfoid);

    int insert(TiProductshowtemplateinfo record);

    int insertSelective(TiProductshowtemplateinfo record);

    TiProductshowtemplateinfo selectByPrimaryKey(Integer tempinfoid);

    int updateByPrimaryKeySelective(TiProductshowtemplateinfo record);

    int updateByPrimaryKey(TiProductshowtemplateinfo record);
    /**
     * 根据tempId获取产品 展示列表
     * @param tempId
     * @return
     */
    List<TiProductshowtemplateinfo> selectByTempId(@Param("tempId")Integer tempId);
}