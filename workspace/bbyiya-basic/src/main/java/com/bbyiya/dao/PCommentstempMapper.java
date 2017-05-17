package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PCommentstemp;

public interface PCommentstempMapper {
    int deleteByPrimaryKey(Integer tipclassid);

    int insert(PCommentstemp record);

    int insertSelective(PCommentstemp record);

    PCommentstemp selectByPrimaryKey(Integer tipclassid);

    int updateByPrimaryKeySelective(PCommentstemp record);

    int updateByPrimaryKey(PCommentstemp record);
    /**
     * 获取评论模板List
     * @param productid
     * @return
     */
    List<PCommentstemp> findTempsByProductId(@Param("productid")Long productid);
}