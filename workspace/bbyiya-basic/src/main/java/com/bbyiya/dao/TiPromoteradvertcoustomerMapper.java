package com.bbyiya.dao;

import java.util.List;

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
    
    /**
     * 根据广告id得到报名客户
     * @param advertid
     * @return
     */
    List<TiPromoteradvertcoustomer> selectListByAdvertId(@Param("advertid")Integer advertid);
}