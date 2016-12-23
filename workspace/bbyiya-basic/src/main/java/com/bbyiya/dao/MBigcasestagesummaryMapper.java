package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.MBigcasestagesummary;
import com.bbyiya.vo.bigcase.BigcasesummaryResult;

public interface MBigcasestagesummaryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MBigcasestagesummary record);

    int insertSelective(MBigcasestagesummary record);

    MBigcasestagesummary selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MBigcasestagesummary record);

    int updateByPrimaryKeyWithBLOBs(MBigcasestagesummary record);

    int updateByPrimaryKey(MBigcasestagesummary record);
    /**
     * 根据阶段Id获取总结列表
     * @param stageId
     * @return
     */
    List<BigcasesummaryResult> findStageSummaryByStageId(@Param("stageId")  Integer stageId);
}