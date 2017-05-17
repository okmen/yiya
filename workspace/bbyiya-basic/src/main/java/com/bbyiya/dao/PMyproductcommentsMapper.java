package com.bbyiya.dao;

import com.bbyiya.model.PMyproductcomments;

public interface PMyproductcommentsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PMyproductcomments record);

    int insertSelective(PMyproductcomments record);

    PMyproductcomments selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PMyproductcomments record);

    int updateByPrimaryKey(PMyproductcomments record);
}