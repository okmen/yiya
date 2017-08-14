package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OBranchorders;
import com.bbyiya.vo.order.BranchOrderVo;

public interface OBranchordersMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OBranchorders record);

    int insertSelective(OBranchorders record);

    OBranchorders selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OBranchorders record);

    int updateByPrimaryKey(OBranchorders record);
    
    List<BranchOrderVo>findBranchOrdersByBranchUserId(@Param("branchUserId")Long branchUserId,@Param("status") Integer status,@Param("keywords") String keywords);
}