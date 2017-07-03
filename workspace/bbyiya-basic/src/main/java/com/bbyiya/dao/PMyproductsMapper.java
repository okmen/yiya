package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducts;
import com.bbyiya.vo.product.MyProductResultVo;

public interface PMyproductsMapper {
	int deleteByPrimaryKey(Long cartid);

	int insert(PMyproducts record);

	int insertSelective(PMyproducts record);

	/**
	 * 新增我的作品
	 * 
	 * @param cartid
	 * @return
	 */
	int insertReturnId(PMyproducts record);

	PMyproducts selectByPrimaryKey(Long cartid);

	/**
	 * 根据我的作品Id获取我的作品
	 * @param cartid
	 * @return
	 */
	MyProductResultVo getMyProductResultVo(Long cartid);
	/**
	 * 根据订单编号获取我的作品
	 * @param orderno
	 * @return
	 */
	PMyproducts getMyProductByOrderNo(@Param("orderNo")String orderno);

	/**
	 * 更新操作
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(PMyproducts record);

	int updateByPrimaryKey(PMyproducts record);

	/**
	 * 获取用户作品列表
	 * 
	 * @param userId
	 * @param status
	 * @return
	 */
	List<MyProductResultVo> findMyProductslist(@Param("userId") Long userId, @Param("status") Integer status);
	/**
	 * 
	 * @param userId
	 * @param productId
	 * @param status
	 * @return
	 */
	PMyproducts getMyProductsByProductId(@Param("userId") Long userId,@Param("productId")Long productId, @Param("status") Integer status);

	/**
	 * 获取影楼(客户一对一)分销作品 情况
	 * @param userIds
	 * @param status
	 * @param invitestatus
	 * @return
	 */
	List<MyProductResultVo> findMyProductslistForBranch(@Param("list")List<Long> list,@Param("status") Integer status,@Param("invitestatus")Integer invitestatus,@Param("keywords")String keywords);
	
	/**
	 * 获取影楼员工协助客户的作品列表
	 * @param userIds
	 * @param status
	 * @param invitestatus
	 * @return
	 */
	List<MyProductResultVo> findMyProductsSourceCustomerOfBranch(@Param("list")List<Long> list,@Param("status") Integer status,@Param("invitestatus")Integer invitestatus,@Param("keywords")String keywords);
	/**
	 * 获取模板下的已获取客户作品
	 * @param userIds
	 * @param status
	 * @param invitestatus
	 * @return
	 */
	List<MyProductResultVo> findMyProductslistForTempId(@Param("list")List<Long> list,@Param("tempid") Integer tempid,@Param("activestatus")Integer activestatus,@Param("keywords")String keywords);
	
	/**
	 * 获取客户受邀请制作中作品列表
	 * @param invitestatus
	 * @return
	 */
	List<MyProductResultVo> findMyInviteProductslist(@Param("inviteUserId")Long inviteUserId,@Param("phone")String phone,@Param("branchUserId")Long branchUserId);
	
	
	List<PMyproducts> findCanOrderMyProducts(@Param("tempid") Integer tempid,@Param("ordertime") Integer ordertime);
}