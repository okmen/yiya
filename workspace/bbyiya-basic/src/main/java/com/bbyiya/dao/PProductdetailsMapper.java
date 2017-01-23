package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PProductdetails;

public interface PProductdetailsMapper {
    int deleteByPrimaryKey(Long detailid);

    int insert(PProductdetails record);

    int insertSelective(PProductdetails record);

    PProductdetails selectByPrimaryKey(Long detailid);

    int updateByPrimaryKeySelective(PProductdetails record);

    int updateByPrimaryKey(PProductdetails record);
    
    /**
     * 获取产品样本图列表
     * @param productId
     * @return
     */
    List<PProductdetails> findDetailsByProductId(@Param("productId") Long productId);
}