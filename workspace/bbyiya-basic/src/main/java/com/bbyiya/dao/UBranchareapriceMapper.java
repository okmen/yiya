package com.bbyiya.dao;

import com.bbyiya.model.UBranchareaprice;

public interface UBranchareapriceMapper {
    int deleteByPrimaryKey(Integer code);

    int insert(UBranchareaprice record);

    int insertSelective(UBranchareaprice record);

    UBranchareaprice selectByPrimaryKey(Integer code);

    int updateByPrimaryKeySelective(UBranchareaprice record);

    int updateByPrimaryKey(UBranchareaprice record);
}