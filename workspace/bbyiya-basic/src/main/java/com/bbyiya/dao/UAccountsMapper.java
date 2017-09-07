package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UAccounts;
import com.bbyiya.vo.user.SmsRemindUserVo;

public interface UAccountsMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UAccounts record);

    int insertSelective(UAccounts record);

    UAccounts selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UAccounts record);

    int updateByPrimaryKey(UAccounts record);
    
    List<SmsRemindUserVo>findSmsRemindUserList(@Param("amount")Double amount);
}