package com.bbyiya.dao;

import com.bbyiya.model.UBranchtransaccounts;

public interface UBranchtransaccountsMapper {
    int deleteByPrimaryKey(Long branchuserid);

    int insert(UBranchtransaccounts record);

    int insertSelective(UBranchtransaccounts record);

    UBranchtransaccounts selectByPrimaryKey(Long branchuserid);

    int updateByPrimaryKeySelective(UBranchtransaccounts record);

    int updateByPrimaryKey(UBranchtransaccounts record);
}