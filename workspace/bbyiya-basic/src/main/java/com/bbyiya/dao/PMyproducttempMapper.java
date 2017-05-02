package com.bbyiya.dao;

import java.util.List;



import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducttemp;

public interface PMyproducttempMapper {
    int deleteByPrimaryKey(Integer tempid);

    int insert(PMyproducttemp record);
    
    int insertReturnId(PMyproducttemp record);

    int insertSelective(PMyproducttemp record);

    PMyproducttemp selectByPrimaryKey(Integer tempid);

    int updateByPrimaryKeySelective(PMyproducttemp record);

    int updateByPrimaryKey(PMyproducttemp record);
    
    /**
     * 获取影楼用户的模板
     * @param branchUserId
     * @return
     */
    List<PMyproducttemp> findBranchMyProductTempList(@Param("branchuserid") Long branchuserid);
}