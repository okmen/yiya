package com.bbyiya.dao;

import com.bbyiya.model.MBigcaseimgs;

public interface MBigcaseimgsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MBigcaseimgs record);

    int insertSelective(MBigcaseimgs record);

    MBigcaseimgs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MBigcaseimgs record);

    int updateByPrimaryKey(MBigcaseimgs record);
}