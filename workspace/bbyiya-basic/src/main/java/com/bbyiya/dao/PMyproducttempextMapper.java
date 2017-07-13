package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducttempext;

public interface PMyproducttempextMapper {
    int deleteByPrimaryKey(Integer tempid);

    int insert(PMyproducttempext record);

    int insertSelective(PMyproducttempext record);

    PMyproducttempext selectByPrimaryKey(Integer tempid);

    int updateByPrimaryKeySelective(PMyproducttempext record);

    int updateByPrimaryKey(PMyproducttempext record);
    
    List<PMyproducttempext> findProductStyleListBytempId(@Param("tempid") Integer tempid);
}