package com.bbyiya.dao;

import com.bbyiya.model.TiProductsext;

public interface TiProductsextMapper {
    int deleteByPrimaryKey(Long productid);

    int insert(TiProductsext record);

    int insertSelective(TiProductsext record);

    TiProductsext selectByPrimaryKey(Long productid);

    int updateByPrimaryKeySelective(TiProductsext record);

    int updateByPrimaryKey(TiProductsext record);
}