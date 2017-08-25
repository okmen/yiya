package com.bbyiya.dao;

import java.util.HashMap;
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
    
    List<PMyproducttemp> findAllTemp(@Param("tempid")Integer tempid);
    /**
     * 根据活动状态得到代理商的活动数量
     * @param agentuserid
     * @param status
     * @return
     */
    Integer getAgentActivityCountByStatus(@Param("agentuserid") Long agentuserid,@Param("status") Integer status);
    
    /**
     * 根据活动类型得到代理商各类活动报名人数
     * @param agentuserid
     * @param status
     * @return
     */
    Integer getAgentActivityApplyCountByType(@Param("agentuserid") Long agentuserid,@Param("type") Integer type);
 
    /**
     * 得到代理商活动的总报名人数
     * @param agentuserid
     * @return
     */
    HashMap<String, Object>getAgentApplyCompleteMap(@Param("agentuserid") Long agentuserid);
    
    /**
     * 代理商举办的所有活动
     * @param agentuserid
     * @return
     */
    List<PMyproducttemp>  findAllTempListByAgentUserId(@Param("agentuserid") Long agentuserid,@Param("status") Integer status);
}