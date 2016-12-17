package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.MBigcaseclasstag;

public interface MBigcaseclasstagMapper {
    int deleteByPrimaryKey(Integer tagid);

    int insert(MBigcaseclasstag record);

    int insertSelective(MBigcaseclasstag record);

    MBigcaseclasstag selectByPrimaryKey(Integer tagid);

    int updateByPrimaryKeySelective(MBigcaseclasstag record);

    int updateByPrimaryKey(MBigcaseclasstag record);
    
    /**
     * 获取大事件标签（根据大事件类型Id回去）
     * @param typeId
     * @return
     */
    List<MBigcaseclasstag> findTagsByClassId(@Param("typeId") Integer typeId);
}