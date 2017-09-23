package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiProductstyles;

public interface TiProductstylesMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(TiProductstyles record);

    int insertSelective(TiProductstyles record);

    TiProductstyles selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(TiProductstyles record);

    int updateByPrimaryKey(TiProductstyles record);
    
    List<TiProductstyles> findAllStylelistByProductId(@Param("productId") Long productId);
    List<TiProductstyles> findStylelistByProductId(@Param("productId") Long productId);
    
    Long getMaxStyleIdByProductId(@Param("productId") Long productId);
}