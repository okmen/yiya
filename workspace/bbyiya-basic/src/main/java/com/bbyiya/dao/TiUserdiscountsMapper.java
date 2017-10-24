package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiUserdiscounts;

public interface TiUserdiscountsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiUserdiscounts record);

    int insertSelective(TiUserdiscounts record);

    TiUserdiscounts selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiUserdiscounts record);

    int updateByPrimaryKey(TiUserdiscounts record);
    /**
     * 我的优惠券
     * @param userId
     * @return
     */
    List<TiUserdiscounts> findMyDiscounts(@Param("userId")Long userId);
    /**
     * 针对某个作品获取的优惠券
     * @param userId
     * @param workId
     * @return
     */
    List<TiUserdiscounts> findMyDiscountsByWorkId(@Param("userId")Long userId,@Param("workId")Long workId);
    
    List<TiUserdiscounts> findMyDiscountsByActId(@Param("userId")Long userId,@Param("actId")Integer actId);
    /**
     * 获取订单使用的优惠券
     * @param userorderId
     * @return
     */
    TiUserdiscounts getMyDiscountsByUserOrderId(@Param("userorderId")String userorderId);
}