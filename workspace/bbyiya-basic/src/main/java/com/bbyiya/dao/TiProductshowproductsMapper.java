package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiProductshowproducts;

public interface TiProductshowproductsMapper {
    int deleteByPrimaryKey(Integer cateid);

    int insert(TiProductshowproducts record);

    int insertSelective(TiProductshowproducts record);

    TiProductshowproducts selectByPrimaryKey(Integer cateid);

    int updateByPrimaryKeySelective(TiProductshowproducts record);

    int updateByPrimaryKey(TiProductshowproducts record);
    
    List<TiProductshowproducts> selectAll();
}