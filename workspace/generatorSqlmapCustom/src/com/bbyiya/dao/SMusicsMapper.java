package com.bbyiya.dao;

import com.bbyiya.model.SMusics;

public interface SMusicsMapper {
    int deleteByPrimaryKey(Integer musicid);

    int insert(SMusics record);

    int insertSelective(SMusics record);

    SMusics selectByPrimaryKey(Integer musicid);

    int updateByPrimaryKeySelective(SMusics record);

    int updateByPrimaryKey(SMusics record);
}