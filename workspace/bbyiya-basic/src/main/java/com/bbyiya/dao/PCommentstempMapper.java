package com.bbyiya.dao;

import com.bbyiya.model.PCommentstemp;

public interface PCommentstempMapper {
    int deleteByPrimaryKey(Integer tipclassid);

    int insert(PCommentstemp record);

    int insertSelective(PCommentstemp record);

    PCommentstemp selectByPrimaryKey(Integer tipclassid);

    int updateByPrimaryKeySelective(PCommentstemp record);

    int updateByPrimaryKey(PCommentstemp record);
}