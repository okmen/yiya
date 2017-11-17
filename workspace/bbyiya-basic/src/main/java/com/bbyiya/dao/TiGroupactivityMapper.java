package com.bbyiya.dao;

import java.util.List;




import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiGroupactivity;

public interface TiGroupactivityMapper {
    int deleteByPrimaryKey(Integer gactid);

    
    int insertReturnId(TiGroupactivity record);
    int insert(TiGroupactivity record);

    int insertSelective(TiGroupactivity record);

    TiGroupactivity selectByPrimaryKey(Integer gactid);

    int updateByPrimaryKeySelective(TiGroupactivity record);

    int updateByPrimaryKeyWithBLOBs(TiGroupactivity record);

    int updateByPrimaryKey(TiGroupactivity record);
    
    List<TiGroupactivity> findGroupActivityList(@Param("promoteruserid") Long promoteruserid,@Param("status") Integer status,@Param("keywords") String keywords);
}