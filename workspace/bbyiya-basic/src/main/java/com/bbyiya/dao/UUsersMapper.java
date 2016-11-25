package com.bbyiya.dao;

import com.bbyiya.model.UUsers;

public interface UUsersMapper {
	/**
	 * 删除用户
	 * @param userid
	 * @return
	 */
	int deleteByPrimaryKey(Long userid);
	/**
	 * 新增用户
	 * @param record
	 * @return
	 */
	int insert(UUsers record);
	/**
	 * 新增用户 返回自增主键userId
	 * @param record
	 * @return
	 */
	int insertReturnKeyId(UUsers record);

	int insertSelective(UUsers record);
	
	UUsers selectByPrimaryKey(Long userid);

	UUsers getUUsersByUserID(Long userid);

	UUsers getUUsersByUserName(String username);

	int updateByPrimaryKeySelective(UUsers record);

	int updateByPrimaryKey(UUsers record);
}