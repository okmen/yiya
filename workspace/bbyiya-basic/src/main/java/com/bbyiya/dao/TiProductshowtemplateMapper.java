package com.bbyiya.dao;

import com.bbyiya.model.TiProductshowtemplate;

public interface TiProductshowtemplateMapper {
    int deleteByPrimaryKey(Integer tempid);

    int insert(TiProductshowtemplate record);

    int insertSelective(TiProductshowtemplate record);

    TiProductshowtemplate selectByPrimaryKey(Integer tempid);

    int updateByPrimaryKeySelective(TiProductshowtemplate record);

    int updateByPrimaryKey(TiProductshowtemplate record);
}