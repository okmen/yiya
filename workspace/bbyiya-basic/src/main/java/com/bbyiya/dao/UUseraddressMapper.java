package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.UUseraddress;

public interface UUseraddressMapper {
	
	/**
	 * 获取用户收货地址
	 * @param addressId
	 * @return
	 */
	UUseraddress get_UUserAddressByKeyId(Long addressId);
	/**
	 * 删除
	 * @param addrid
	 * @return
	 */
    int deleteByPrimaryKey(Long addrid);
    /**
     * 新增
     * @param record
     * @return
     */
    int insert(UUseraddress record);

    int insertSelective(UUseraddress record);
    /**
     * 
     * @param record
     * @return
     */
    int insertReturnId(UUseraddress record);

    /**
     * 修改改动的字段
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(UUseraddress record);
    /**
     * 修改所有字段
     * @param record
     * @return
     */
    int updateByPrimaryKey(UUseraddress record);
    
    /**
     * 获取用户的收货地址
     * @param userId
     * @return
     */
    List<UUseraddress> find_UUserAddressByUserId(Long userId);
}