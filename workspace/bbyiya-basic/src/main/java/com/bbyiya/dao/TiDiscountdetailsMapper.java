package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiDiscountdetails;

public interface TiDiscountdetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiDiscountdetails record);

    int insertSelective(TiDiscountdetails record);

    TiDiscountdetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiDiscountdetails record);

    int updateByPrimaryKey(TiDiscountdetails record);
    
    /**
     * 优惠券具体详情
     * @param discountId
     * @return
     */
    List<TiDiscountdetails> findDiscountlist(@Param("discountId")Integer discountId);
}