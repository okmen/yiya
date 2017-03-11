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
    List<PMyproductsinvites> findListByCartId(@Param("cartId")Long cartId);
}