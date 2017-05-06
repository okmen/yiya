package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.PStandards;
import com.bbyiya.vo.product.ProductStandardResult;

public interface PStandardsMapper {
    int deleteByPrimaryKey(Long standardid);

    int insert(PStandards record);

    int insertSelective(PStandards record);

    PStandards selectByPrimaryKey(Long standardid);

    int updateByPrimaryKeySelective(PStandards record);

    int updateByPrimaryKey(PStandards record);
    /**
     * 获取属性值列表
     * @param standardid
     * @return
     */
    List<ProductStandardResult> findStandardResult(Long standardid);
}