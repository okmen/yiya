package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.PProductstyleproperty;

public interface PProductstylepropertyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PProductstyleproperty record);

    int insertSelective(PProductstyleproperty record);

    PProductstyleproperty selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PProductstyleproperty record);

    int updateByPrimaryKey(PProductstyleproperty record);
    /**
     * 获取产品的所有属性
     * @param productId
     * @return
     */
    List<PProductstyleproperty> findStylePropertyByProductId(Long productId);
}