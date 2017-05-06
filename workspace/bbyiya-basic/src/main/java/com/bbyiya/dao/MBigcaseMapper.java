package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.MBigcase;
import com.bbyiya.vo.bigcase.BigcaseResult;

public interface MBigcaseMapper {
	
    int deleteByPrimaryKey(Integer caseid);

    int insert(MBigcase record);

    int insertSelective(MBigcase record);

    MBigcase selectByPrimaryKey(Integer caseid);

    int updateByPrimaryKeySelective(MBigcase record);

    int updateByPrimaryKey(MBigcase record);
    
    /**
     * 获取大事件详情
     * @param caseid
     * @return
     */
    BigcaseResult getMBigcaseResultByCaseId(Integer caseid);
    /**
     * 获取大事件列表
     * @param start
     * @param end
     * @return
     */
    List<MBigcase> findMBigcaseList(@Param("start")Integer start,@Param("end")Integer  end);
    /**
     * 获取宝宝大事件列表
     * @param start
     * @param end
     * @return
     */
    List<BigcaseResult> findMBigcaseResultList(@Param("start")Integer start,@Param("end")Integer  end);
}