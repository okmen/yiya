package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.UOtherlogin;
import com.bbyiya.vo.user.OtherLoginParam;

public interface UOtherloginMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UOtherlogin record);

    int insertSelective(UOtherlogin record);

    UOtherlogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UOtherlogin record);

    int updateByPrimaryKey(UOtherlogin record);
    
    UOtherlogin get_UOtherlogin(OtherLoginParam param);
}