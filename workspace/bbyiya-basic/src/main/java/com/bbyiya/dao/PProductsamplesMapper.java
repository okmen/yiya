package com.bbyiya.dao;

import com.bbyiya.model.PProductsamples;

public interface PProductsamplesMapper {
    int deleteByPrimaryKey(Integer sampleid);

    int insert(PProductsamples record);

    int insertSelective(PProductsamples record);

    PProductsamples selectByPrimaryKey(Integer sampleid);

    int updateByPrimaryKeySelective(PProductsamples record);

    int updateByPrimaryKey(PProductsamples record);
}