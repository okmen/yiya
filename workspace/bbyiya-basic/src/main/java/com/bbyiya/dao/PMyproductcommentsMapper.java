package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproductcomments;

public interface PMyproductcommentsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PMyproductcomments record);

    int insertSelective(PMyproductcomments record);

    PMyproductcomments selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PMyproductcomments record);

    int updateByPrimaryKey(PMyproductcomments record);
    /**
     * 作品评论列表
     * @param cartid
     * @return
     */
    List<PMyproductcomments> findCommentlist(@Param("cartid")Long cartid);
    /**
     * 评论者头像列表
     * @param cartid
     * @return
     */
    List<PMyproductcomments> findCommentHeadImglist(@Param("cartid")Long cartid);
}