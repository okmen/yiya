package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.bbyiya.model.UUserresponses;

public interface UUserresponsesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UUserresponses record);

    int insertSelective(UUserresponses record);

    UUserresponses selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UUserresponses record);

    int updateByPrimaryKey(UUserresponses record);
    
    List<UUserresponses> findUserResponse(@Param("startTimeStr") String startTimeStr,@Param("endTimeStr") String endTimeStr);
}