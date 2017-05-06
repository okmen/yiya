package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.UWeiusers;
import com.bbyiya.vo.user.UWeiUserSearchParam;

public interface UWeiusersMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UWeiusers record);

    int insertSelective(UWeiusers record);

    UWeiusers selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UWeiusers record);

    int updateByPrimaryKey(UWeiusers record);
    
    List<UWeiusers> findUWeiusersList(UWeiUserSearchParam param);
}