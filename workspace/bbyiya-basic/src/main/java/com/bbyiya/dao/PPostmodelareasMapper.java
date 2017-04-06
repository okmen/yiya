package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PPostmodelareas;

public interface PPostmodelareasMapper {
    int deleteByPrimaryKey(Integer postid);

    int insert(PPostmodelareas record);

    int insertSelective(PPostmodelareas record);

    PPostmodelareas selectByPrimaryKey(Integer postid);

    int updateByPrimaryKeySelective(PPostmodelareas record);

    int updateByPrimaryKey(PPostmodelareas record);
    /**
     * 根据区域、快递方式 查询特殊区域价格
     * @param postmodelid
     * @param area
     * @return
     */
    PPostmodelareas getPostAreaModel(@Param("postmodelId")Integer postmodelid, @Param("area")Integer area);
}