package com.bbyiya.dao;

import java.util.List;

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
    
    /**
     * 获取用户收货地址列表
     * @param ids
     * @return
     */
    List<OOrderaddress> findListByIds(List<Long> ids);
}