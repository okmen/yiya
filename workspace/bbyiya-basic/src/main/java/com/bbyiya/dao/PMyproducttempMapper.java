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
    List<PMyproducttemp> findBranchMyProductTempList(@Param("branchuserid") Long branchuserid,@Param("status") Integer status,@Param("keywords") String keywords,@Param("type")Integer type);
    /**
     * 获取影楼管理员 需审核的异业合作模板列表
     * @param branchuserid
     * @return
     */
    List<PMyproducttemp> findBranchMyProductTempNeedVerList(@Param("branchuserid") Long branchuserid);
    /**
     * 获取影楼 员工需审核的异业合作模板列表
     * @param branchuserid
     * @return
     */
    List<PMyproducttemp> findBranchUserMyProductTempNeedVerList(@Param("userid") Long branchuserid);
    
    /**
     *  根据状态获取自动下单的所有活动列表
     * @param branchuserid
     * @return
     */
    List<PMyproducttemp> findAllAutoOrderTempByStatus(@Param("status") Integer status);
}