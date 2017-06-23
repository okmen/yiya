package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.DMyproductdiscountmodel;

public interface DMyproductdiscountmodelMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(DMyproductdiscountmodel record);

    int insertSelective(DMyproductdiscountmodel record);

    DMyproductdiscountmodel selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(DMyproductdiscountmodel record);

    int updateByPrimaryKey(DMyproductdiscountmodel record);
    /**
     * 通过阶段 获取款式折扣列表
     * @param productId
     * @return
     */
    List<DMyproductdiscountmodel> findListByProductId(@Param("productId")Long productId);
}