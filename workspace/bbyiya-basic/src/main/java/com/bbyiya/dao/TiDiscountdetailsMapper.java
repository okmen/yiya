package com.bbyiya.dao;

import com.bbyiya.model.TiDiscountdetails;

public interface TiDiscountdetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiDiscountdetails record);

    int insertSelective(TiDiscountdetails record);

    TiDiscountdetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiDiscountdetails record);

    int updateByPrimaryKey(TiDiscountdetails record);
}