package com.bbyiya.dao;

import com.bbyiya.model.TiProductareas;

public interface TiProductareasMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiProductareas record);

    int insertSelective(TiProductareas record);

    TiProductareas selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiProductareas record);

    int updateByPrimaryKey(TiProductareas record);
}