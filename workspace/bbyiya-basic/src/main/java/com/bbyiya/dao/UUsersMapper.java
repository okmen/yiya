package com.bbyiya.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UUsers;

public interface UUsersMapper {
	
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
	/**
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(UUsers record);
	
	UUsers selectByPrimaryKey(Long userid);

	/**
	 * 根据用户userId获取用户
	 * @param userid
	 * @return
	 */
	UUsers getUUsersByUserID(Long userid);

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	UUsers getUUsersByUserName(String username);
	/**
	 * 根据手机好获取用户
	 * @param phone
	 * @return
	 */
	UUsers getUUsersByPhone(String phone);

	/**
	 * 对局部赋值的字段进行更新 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(UUsers record);
	/**
	 * 全部字段更新
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(UUsers record);

	/**
	 * 查询我推荐的用户列表
	 * @param upUserid
	 * @return
	 */
	List<UUsers> findUUsersByUpUserid(@Param("upid") Long upUserid,@Param("startTime")Date starttime,@Param("endTime")Date endtime);
	
	/**
	 * 查询最终我发展的用户
	 * @param upUserid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	List<UUsers> findUUsersBySourceUserId(@Param("sourceuserid") Long sourceuserid,@Param("startTime")Date starttime,@Param("endTime")Date endtime);
	/**
	 * 获取我发展的用户数
	 * @param branchuserid
	 * @return
	 */
	Integer getUserCountByUpUserid(@Param("upid") Long upUserid);
}