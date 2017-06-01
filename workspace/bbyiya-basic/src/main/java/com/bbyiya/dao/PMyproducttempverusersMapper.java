package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducttempverusers;

public interface PMyproducttempverusersMapper {
    int deleteByPrimaryKey(Long verid);

    int insert(PMyproducttempverusers record);

    int insertSelective(PMyproducttempverusers record);

    PMyproducttempverusers selectByPrimaryKey(Long verid);

    int updateByPrimaryKeySelective(PMyproducttempverusers record);

    int updateByPrimaryKey(PMyproducttempverusers record);
    
    PMyproducttempverusers selectByUserIdAndTempId(@Param("userId") Long userId,@Param("tempId")int tempId);
    
    List<PMyproducttempverusers> findTempVerfiyUserListByTempId(@Param("tempId")int tempId);
    
}