package com.bbyiya.dao;

import com.bbyiya.model.TiPromoteremployees;

public interface TiPromoteremployeesMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(TiPromoteremployees record);

    int insertSelective(TiPromoteremployees record);

    TiPromoteremployees selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(TiPromoteremployees record);

    int updateByPrimaryKey(TiPromoteremployees record);
}