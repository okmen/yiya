package com.bbyiya.dao;

import com.bbyiya.model.UCashlogs;

public interface UCashlogsMapper {
    int deleteByPrimaryKey(Long cashlogid);

    int insert(UCashlogs record);

    int insertSelective(UCashlogs record);

    UCashlogs selectByPrimaryKey(Long cashlogid);

    int updateByPrimaryKeySelective(UCashlogs record);

    int updateByPrimaryKey(UCashlogs record);
}