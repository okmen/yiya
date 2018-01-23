package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiProductblessings;

public interface TiProductblessingsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiProductblessings record);

    int insertSelective(TiProductblessings record);

    TiProductblessings selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiProductblessings record);

    int updateByPrimaryKey(TiProductblessings record);
    /**
     * 获取所有的祝福语模板
     * @return
     */
    List<TiProductblessings> findAll();
}