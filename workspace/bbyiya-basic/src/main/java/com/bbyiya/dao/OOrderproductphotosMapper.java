package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.bbyiya.model.OOrderproductphotos;

public interface OOrderproductphotosMapper {
    int deleteByPrimaryKey(Long odetailid);

    int insert(OOrderproductphotos record);

    int insertSelective(OOrderproductphotos record);

    OOrderproductphotos selectByPrimaryKey(Long odetailid);

    int updateByPrimaryKeySelective(OOrderproductphotos record);

    int updateByPrimaryKey(OOrderproductphotos record);
    
    /**
     * 查询订单图片
     * @param orderproductId
     * @return
     */
    List<OOrderproductphotos> findOrderProductPhotosByProductOrderId(@Param("orderProductId") String orderproductId);
    
    List<OOrderproductphotos> findImgNotGood();
}