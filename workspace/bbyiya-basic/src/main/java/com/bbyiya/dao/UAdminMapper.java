package com.bbyiya.dao;

import com.bbyiya.model.UAdmin;

public interface UAdminMapper {
    int deleteByPrimaryKey(Integer adminid);

    int insert(UAdmin record);

    int insertSelective(UAdmin record);

    UAdmin selectByPrimaryKey(Integer adminid);

    int updateByPrimaryKeySelective(UAdmin record);

    int updateByPrimaryKey(UAdmin record);
    /**
     * 获取管理员用户
     * @param username
     * @return
     */
    UAdmin getUAdminByUsername(String username);
}