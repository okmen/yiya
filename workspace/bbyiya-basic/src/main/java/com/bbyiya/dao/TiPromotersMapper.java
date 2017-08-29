package com.bbyiya.dao;

import com.bbyiya.model.TiPromoters;

public interface TiPromotersMapper {
    int deleteByPrimaryKey(Long promoteruserid);

    int insert(TiPromoters record);

    int insertSelective(TiPromoters record);

    TiPromoters selectByPrimaryKey(Long promoteruserid);

    int updateByPrimaryKeySelective(TiPromoters record);

    int updateByPrimaryKey(TiPromoters record);
}