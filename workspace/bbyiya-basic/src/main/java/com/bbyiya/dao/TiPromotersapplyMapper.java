package com.bbyiya.dao;

import com.bbyiya.model.TiPromotersapply;

public interface TiPromotersapplyMapper {
    int deleteByPrimaryKey(Long promoteruserid);

    int insert(TiPromotersapply record);

    int insertSelective(TiPromotersapply record);

    TiPromotersapply selectByPrimaryKey(Long promoteruserid);

    int updateByPrimaryKeySelective(TiPromotersapply record);

    int updateByPrimaryKey(TiPromotersapply record);
}