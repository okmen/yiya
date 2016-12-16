package com.bbyiya.dao;

import com.bbyiya.model.MBigcaseuserimgs;

public interface MBigcaseuserimgsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MBigcaseuserimgs record);

    int insertSelective(MBigcaseuserimgs record);

    MBigcaseuserimgs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MBigcaseuserimgs record);

    int updateByPrimaryKey(MBigcaseuserimgs record);
}