package com.bbyiya.dao;

import com.bbyiya.model.TiMyartsdetails;

public interface TiMyartsdetailsMapper {
    int deleteByPrimaryKey(Long detailid);

    int insert(TiMyartsdetails record);

    int insertSelective(TiMyartsdetails record);

    TiMyartsdetails selectByPrimaryKey(Long detailid);

    int updateByPrimaryKeySelective(TiMyartsdetails record);

    int updateByPrimaryKey(TiMyartsdetails record);
}