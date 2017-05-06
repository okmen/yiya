package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.EErrors;

public interface EErrorsMapper {
    int deleteByPrimaryKey(Long logid);

    int insert(EErrors record);

    int insertSelective(EErrors record);

    EErrors selectByPrimaryKey(Long logid);

    int updateByPrimaryKeySelective(EErrors record);

    int updateByPrimaryKey(EErrors record);
    /**
     * 新增错误记录
     * @param classname
     * @param msg
     * @return
     */
    int addError(@Param("classname")String classname,@Param("msg")String msg);
}