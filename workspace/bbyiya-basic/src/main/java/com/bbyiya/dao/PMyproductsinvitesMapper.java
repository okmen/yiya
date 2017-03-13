package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproductsinvites;

public interface PMyproductsinvitesMapper {
    int deleteByPrimaryKey(Long inviteid);

    int insert(PMyproductsinvites record);

    int insertSelective(PMyproductsinvites record);

    PMyproductsinvites selectByPrimaryKey(Long inviteid);

    int updateByPrimaryKeySelective(PMyproductsinvites record);

    int updateByPrimaryKey(PMyproductsinvites record);
    /**
     * 获取我的作品邀请列表
     * @param cartId
     * @return
     */
    List<PMyproductsinvites> findListByCartId(@Param("cartId") Long cartId);
    /**
     * 根据手机号 获取被邀请协同编辑的 作品列表
     * @param phone
     * @return
     */
    List<PMyproductsinvites> findListByPhone(@Param("phone") String phone);
    /**
     * 查找我的邀约数量
     * @param phone
     * @return
     */
    int countInvitingsByPhone(@Param("phone")String phone,@Param("status")Integer status);
    /**
     * 获取邀请model
     * @param phone
     * @param cartId
     * @return
     */
    PMyproductsinvites getInviteByPhoneAndCartId(@Param("phone")String phone,@Param("cartId")Long cartId);
}