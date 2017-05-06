package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproductdetails;

public interface PMyproductdetailsMapper {
    int deleteByPrimaryKey(Long pdid);
    
    int deleteByCartId(@Param("cartId") Long cartId);

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
    /**
     * 获取作品的图片列表
     * @param cartid
     * @return
     */
    List<PMyproductdetails> findMyProductdetails(@Param("cartId")Long cartid);
    
}