package com.bbyiya.dao;

import com.bbyiya.model.OOrderaddress;

public interface OOrderaddressMapper {
    int deleteByPrimaryKey(Long orderaddressid);

    int insert(OOrderaddress record);

    int insertSelective(OOrderaddress record);
    /**
     * 新增订单收货地址 并返回主键Id
     * @param record
     * @return
     */
    int insertReturnId(OOrderaddress record);

    OOrderaddress selectByPrimaryKey(Long orderaddressid);

    int updateByPrimaryKeySelective(OOrderaddress record);

    int updateByPrimaryKey(OOrderaddress record);
}