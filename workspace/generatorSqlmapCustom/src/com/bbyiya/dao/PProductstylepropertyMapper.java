package com.bbyiya.dao;

import com.bbyiya.model.PProductstyleproperty;

public interface PProductstylepropertyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PProductstyleproperty record);

    int insertSelective(PProductstyleproperty record);

    PProductstyleproperty selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PProductstyleproperty record);

    int updateByPrimaryKey(PProductstyleproperty record);
}