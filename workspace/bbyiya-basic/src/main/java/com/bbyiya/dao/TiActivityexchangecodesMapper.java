package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiActivityexchangecodes;

public interface TiActivityexchangecodesMapper {
    int deleteByPrimaryKey(String codenum);

    int insert(TiActivityexchangecodes record);

    int insertSelective(TiActivityexchangecodes record);

    TiActivityexchangecodes selectByPrimaryKey(String codenum);

    int updateByPrimaryKeySelective(TiActivityexchangecodes record);

    int updateByPrimaryKey(TiActivityexchangecodes record);
    
    /**
     * 根据活动ID得到兑换码
     * @param actid
     * @return
     */
    List<TiActivityexchangecodes>findTiActivityExchangeCodeList(@Param("actid")Integer actid);
}