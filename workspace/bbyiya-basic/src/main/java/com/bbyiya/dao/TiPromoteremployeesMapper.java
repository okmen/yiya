package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiPromoteremployees;

public interface TiPromoteremployeesMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(TiPromoteremployees record);

    int insertSelective(TiPromoteremployees record);

    TiPromoteremployees selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(TiPromoteremployees record);

    int updateByPrimaryKey(TiPromoteremployees record);
    
    List<TiPromoteremployees>findEmployeelistByPromoterUserId(@Param("promoteruserid")Long  promoteruserid);
}