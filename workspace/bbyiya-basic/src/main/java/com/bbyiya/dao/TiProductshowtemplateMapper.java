package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiProductshowtemplate;

public interface TiProductshowtemplateMapper {
    int deleteByPrimaryKey(Integer tempid);

    int insert(TiProductshowtemplate record);
    
    int insertReturnId(TiProductshowtemplate record);

    int insertSelective(TiProductshowtemplate record);

    TiProductshowtemplate selectByPrimaryKey(Integer tempid);

    int updateByPrimaryKeySelective(TiProductshowtemplate record);

    int updateByPrimaryKey(TiProductshowtemplate record);
    
    List<TiProductshowtemplate> selectByAll(@Param("keywords") String keywords);
}