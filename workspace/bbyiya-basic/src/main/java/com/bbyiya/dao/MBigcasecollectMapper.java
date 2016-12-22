package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.MBigcasecollect;

public interface MBigcasecollectMapper {
    int deleteByPrimaryKey(Long collectid);

    int insert(MBigcasecollect record);

    int insertSelective(MBigcasecollect record);

    MBigcasecollect selectByPrimaryKey(Long collectid);

    int updateByPrimaryKeySelective(MBigcasecollect record);

    int updateByPrimaryKey(MBigcasecollect record);
    /**
     * 获取用户收藏列表
     * @param userid
     * @return
     */
    List<MBigcasecollect> findMBigCaseCollect(@Param("userId")Long userid);
    
}