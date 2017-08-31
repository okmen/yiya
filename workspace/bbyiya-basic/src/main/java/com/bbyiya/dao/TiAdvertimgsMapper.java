package com.bbyiya.dao;

import com.bbyiya.model.TiAdvertimgs;

public interface TiAdvertimgsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiAdvertimgs record);

    int insertSelective(TiAdvertimgs record);

    TiAdvertimgs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiAdvertimgs record);

    int updateByPrimaryKey(TiAdvertimgs record);
}