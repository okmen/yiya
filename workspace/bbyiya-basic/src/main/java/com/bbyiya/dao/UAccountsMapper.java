package com.bbyiya.dao;

import com.bbyiya.model.UAccounts;

public interface UAccountsMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UAccounts record);

    int insertSelective(UAccounts record);

    UAccounts selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UAccounts record);

    int updateByPrimaryKey(UAccounts record);
}