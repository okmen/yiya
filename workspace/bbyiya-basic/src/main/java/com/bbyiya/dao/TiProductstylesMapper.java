package com.bbyiya.dao;

import com.bbyiya.model.TiProductstyles;

public interface TiProductstylesMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(TiProductstyles record);

    int insertSelective(TiProductstyles record);

    TiProductstyles selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(TiProductstyles record);

    int updateByPrimaryKeyWithBLOBs(TiProductstyles record);

    int updateByPrimaryKey(TiProductstyles record);
}