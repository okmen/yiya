package com.bbyiya.dao;

import com.bbyiya.model.TiPromoteradvertimgs;

public interface TiPromoteradvertimgsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiPromoteradvertimgs record);

    int insertSelective(TiPromoteradvertimgs record);

    TiPromoteradvertimgs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiPromoteradvertimgs record);

    int updateByPrimaryKey(TiPromoteradvertimgs record);
}