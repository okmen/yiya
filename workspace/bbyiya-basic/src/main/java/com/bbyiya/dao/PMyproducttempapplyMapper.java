package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducttempapply;

public interface PMyproducttempapplyMapper {
    int deleteByPrimaryKey(Long tempapplyid);

    int insert(PMyproducttempapply record);

    int insertSelective(PMyproducttempapply record);

    PMyproducttempapply selectByPrimaryKey(Long tempapplyid);

    int updateByPrimaryKeySelective(PMyproducttempapply record);

    int updateByPrimaryKey(PMyproducttempapply record);
    /**
     * 得到模板待审核的报名人数
     * @param tempid
     * @return
     */
    Integer getNeedCheckApllyCountByTemp(@Param("tempid") Integer tempid,@Param("status") Integer status);
    /**
     * 根据模板ID及状态得到待审核用户列表
     * @param tempid
     * @return
     */
    List<PMyproducttempapply> findMyProducttempApplyList(@Param("tempid") Integer tempid,@Param("status") Integer status);
}