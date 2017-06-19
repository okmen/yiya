package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproductactivitycode;

public interface PMyproductactivitycodeMapper {
    int deleteByPrimaryKey(String codeno);

    int insert(PMyproductactivitycode record);

    int insertSelective(PMyproductactivitycode record);

    PMyproductactivitycode selectByPrimaryKey(String codeno);

    int updateByPrimaryKeySelective(PMyproductactivitycode record);

    int updateByPrimaryKey(PMyproductactivitycode record);
    
    
    List<PMyproductactivitycode> findActivitycodelistForTempId(@Param("tempid") Integer tempid,@Param("activestatus")Integer invitestatus,@Param("keywords")String keywords);
}