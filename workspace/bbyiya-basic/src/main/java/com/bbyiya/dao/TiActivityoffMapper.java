package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiActivityoff;

public interface TiActivityoffMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiActivityoff record);

    int insertSelective(TiActivityoff record);

    TiActivityoff selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiActivityoff record);

    int updateByPrimaryKey(TiActivityoff record);
    
    TiActivityoff selectByPromoterUserIdAndActId(@Param("promoteruserid")Long  promoteruserid,@Param("actid")Integer  actid);
}