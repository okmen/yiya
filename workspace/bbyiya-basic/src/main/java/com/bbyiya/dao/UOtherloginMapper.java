package com.bbyiya.dao;


import com.bbyiya.model.UOtherlogin;
import com.bbyiya.vo.user.OtherLoginParam;

public interface UOtherloginMapper {
    int deleteByPrimaryKey(Long id);

    /**
     * 新增第三方登陆信息
     * @param record
     * @return
     */
    int insert(UOtherlogin record);

    int insertSelective(UOtherlogin record);

    UOtherlogin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UOtherlogin record);

    int updateByPrimaryKey(UOtherlogin record);
    /**
     * 获取第三方登陆信息 
     * @param param
     * @return
     */
    UOtherlogin get_UOtherlogin(OtherLoginParam param);
}