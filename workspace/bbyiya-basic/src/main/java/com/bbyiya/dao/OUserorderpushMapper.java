package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.OUserorderpush;
import com.bbyiya.vo.order.UserOrderRedpackgeResult;

public interface OUserorderpushMapper {
    int deleteByPrimaryKey(String userorderid);

    int insert(OUserorderpush record);

    int insertSelective(OUserorderpush record);

    OUserorderpush selectByPrimaryKey(String userorderid);

    int updateByPrimaryKeySelective(OUserorderpush record);

    int updateByPrimaryKey(OUserorderpush record);
    /**
     * 获取待 推送第三方生产的 红包订单列表
     * @return
     */
    List<UserOrderRedpackgeResult> findUserorderForPushList();
}