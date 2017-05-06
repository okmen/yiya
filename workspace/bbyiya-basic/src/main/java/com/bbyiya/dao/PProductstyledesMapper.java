package com.bbyiya.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PProductstyledes;

public interface PProductstyledesMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PProductstyledes record);

    int insertSelective(PProductstyledes record);

    PProductstyledes selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PProductstyledes record);

    int updateByPrimaryKey(PProductstyledes record);
    
    /**
     * 
     * @param styleid
     * @return
     */
    List<PProductstyledes> findImgsByStyleId(@Param("styleId") Long styleid);
}