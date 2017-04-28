package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.MBigcaseuserimgs;
import com.bbyiya.vo.bigcase.BigcaseUserImgVO;

public interface MBigcaseuserimgsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MBigcaseuserimgs record);

    int insertSelective(MBigcaseuserimgs record);

    MBigcaseuserimgs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MBigcaseuserimgs record);

    int updateByPrimaryKey(MBigcaseuserimgs record);
    /**
     * 获取用户大事件照片
     * @param caseId
     * @param userid
     * @return
     */
    List<BigcaseUserImgVO> findMBigCaseImgsByCaseId(@Param("caseId")Integer caseId,@Param("userId")Long userid);
}