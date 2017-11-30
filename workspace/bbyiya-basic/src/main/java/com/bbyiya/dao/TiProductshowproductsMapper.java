package com.bbyiya.dao;

import com.bbyiya.model.TiProductshowproducts;

public interface TiProductshowproductsMapper {
    int deleteByPrimaryKey(Integer cateid);

    int insert(TiProductshowproducts record);

    int insertSelective(TiProductshowproducts record);

    TiProductshowproducts selectByPrimaryKey(Integer cateid);

    int updateByPrimaryKeySelective(TiProductshowproducts record);

    int updateByPrimaryKey(TiProductshowproducts record);
}