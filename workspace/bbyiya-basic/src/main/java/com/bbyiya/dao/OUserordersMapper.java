package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.OUserorders;
import com.bbyiya.vo.order.UserBuyerOrderResult;

public interface OUserordersMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OUserorders record);

    int insertSelective(OUserorders record);

    OUserorders selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OUserorders record);

    int updateByPrimaryKey(OUserorders record);
    
    /**
     * 获取用户订单列表
     * @param userId
     * @return
     */
    List<OUserorders> findOrderByUserId(@Param("userId") Long userId);
    
    List<UserBuyerOrderResult>findUserOrderByUserId(@Param("userId") Long userId);
    
    /**
     * 待领取的订单
     * @param agentUserId
     * @return
     */
    List<OUserorders> findOrdersByAgentUserId(@Param("agentUserId")Long agentUserId);
    
    /**
     * 获取影楼的订单列表
     * @param branchUserId
     * @return
     */
    List<OUserorders> findOrdersByBranchUserId(@Param("branchUserId")Long branchUserId,@Param("status") Integer status,@Param("keywords") String keywords);
    
    /**
     * 获取最新的几条订单记录
     * @param status
     * @return
     */
    List<OUserorders> findOrderListLasts(@Param("status") Integer status);
    
    /**
     * 获取用户订单列表
     * @param userId
     * @return
     */
    OUserorders findLatelyOrderByUserId(@Param("userId") Long userId);
}