package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.MBigcasetag;

public interface MBigcasetagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MBigcasetag record);

    int insertSelective(MBigcasetag record);

    MBigcasetag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MBigcasetag record);

    int updateByPrimaryKeyWithBLOBs(MBigcasetag record);

    int updateByPrimaryKey(MBigcasetag record);
    /**
     * 根据大事件Id获取标签
     * @param caseId
     * @return
     */
    List<MBigcasetag> findBigCaseTagByCaseId(@Param("caseId")Integer caseId);
}