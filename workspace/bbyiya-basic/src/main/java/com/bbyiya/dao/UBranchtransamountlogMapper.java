package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UBranchtransamountlog;
import com.bbyiya.vo.user.UBranchTansAmountlogResult;

public interface UBranchtransamountlogMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(UBranchtransamountlog record);

    int insertSelective(UBranchtransamountlog record);

    UBranchtransamountlog selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(UBranchtransamountlog record);

    int updateByPrimaryKey(UBranchtransamountlog record);
    
    /**
     *  获取代理商运费账户交易流水
     * @param userId
     * @param type
     * @return
     */
    List<UBranchTansAmountlogResult> findUBranchTansAmountlogResultByBranchUserId(@Param("branchUserId")Long branchUserId,@Param("type")Integer type);
}