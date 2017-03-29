package com.bbyiya.dao;

import com.bbyiya.model.UBranchtransamountlog;

public interface UBranchtransamountlogMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(UBranchtransamountlog record);

    int insertSelective(UBranchtransamountlog record);

    UBranchtransamountlog selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(UBranchtransamountlog record);

    int updateByPrimaryKey(UBranchtransamountlog record);
}