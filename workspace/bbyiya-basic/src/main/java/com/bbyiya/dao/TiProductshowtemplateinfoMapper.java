package com.bbyiya.dao;

import com.bbyiya.model.TiProductshowtemplateinfo;

public interface TiProductshowtemplateinfoMapper {
    int deleteByPrimaryKey(Integer tempinfoid);

    int insert(TiProductshowtemplateinfo record);

    int insertSelective(TiProductshowtemplateinfo record);

    TiProductshowtemplateinfo selectByPrimaryKey(Integer tempinfoid);

    int updateByPrimaryKeySelective(TiProductshowtemplateinfo record);

    int updateByPrimaryKey(TiProductshowtemplateinfo record);
}