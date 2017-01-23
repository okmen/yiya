package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproductdetails;

public interface PMyproductdetailsMapper {
    int deleteByPrimaryKey(Long pdid);

    int insert(PMyproductdetails record);

    int insertSelective(PMyproductdetails record);

    PMyproductdetails selectByPrimaryKey(Long pdid);

    int updateByPrimaryKeySelective(PMyproductdetails record);

    int updateByPrimaryKey(PMyproductdetails record);
    
    /**
     * 获取我的作品的排序最大数
     * @param cartId
     * @return
     */
    Integer getMaxSort(@Param("cartId")Long cartId);
    
}