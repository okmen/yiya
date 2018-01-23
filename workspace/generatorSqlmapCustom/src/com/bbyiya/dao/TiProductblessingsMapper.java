package com.bbyiya.dao;

import com.bbyiya.model.TiProductblessings;

public interface TiProductblessingsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiProductblessings record);

    int insertSelective(TiProductblessings record);

    TiProductblessings selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiProductblessings record);

    int updateByPrimaryKey(TiProductblessings record);
}