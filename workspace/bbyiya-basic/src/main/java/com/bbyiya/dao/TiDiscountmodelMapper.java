package com.bbyiya.dao;

import com.bbyiya.model.TiDiscountmodel;

public interface TiDiscountmodelMapper {
    int deleteByPrimaryKey(Integer discountid);

    int insert(TiDiscountmodel record);

    int insertSelective(TiDiscountmodel record);

    TiDiscountmodel selectByPrimaryKey(Integer discountid);

    int updateByPrimaryKeySelective(TiDiscountmodel record);

    int updateByPrimaryKey(TiDiscountmodel record);
}