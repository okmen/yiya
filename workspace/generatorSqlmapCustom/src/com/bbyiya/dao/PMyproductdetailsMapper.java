package com.bbyiya.dao;

import com.bbyiya.model.PMyproductdetails;

public interface PMyproductdetailsMapper {
    int deleteByPrimaryKey(Long pdid);

    int insert(PMyproductdetails record);

    int insertSelective(PMyproductdetails record);

    PMyproductdetails selectByPrimaryKey(Long pdid);

    int updateByPrimaryKeySelective(PMyproductdetails record);

    int updateByPrimaryKey(PMyproductdetails record);
}