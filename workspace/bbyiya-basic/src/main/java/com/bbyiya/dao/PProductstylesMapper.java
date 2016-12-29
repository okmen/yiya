package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.PProductstyles;

public interface PProductstylesMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(PProductstyles record);

    int insertSelective(PProductstyles record);

    PProductstyles selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(PProductstyles record);

    int updateByPrimaryKey(PProductstyles record);
    /**
     * 获取款式列表
     * @param productId
     * @return
     */
    List<PProductstyles> findStylesByProductId(Long productId);
}