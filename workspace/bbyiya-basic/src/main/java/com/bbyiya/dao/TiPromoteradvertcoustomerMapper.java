package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiPromoteradvertcoustomer;

public interface TiPromoteradvertcoustomerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiPromoteradvertcoustomer record);

    int insertSelective(TiPromoteradvertcoustomer record);

    TiPromoteradvertcoustomer selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiPromoteradvertcoustomer record);

    int updateByPrimaryKey(TiPromoteradvertcoustomer record);
    /**
     * 通过手机号查询
     * @param advertId
     * @param phone
     * @return
     */
    TiPromoteradvertcoustomer getCustomerByPhone(@Param("advertId")Integer advertId,@Param("phone")String phone);
}