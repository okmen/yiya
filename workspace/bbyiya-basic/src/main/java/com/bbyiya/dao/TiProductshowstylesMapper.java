package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiProductshowstyles;

public interface TiProductshowstylesMapper {
    int deleteByPrimaryKey(Integer showstyleid);

    int insert(TiProductshowstyles record);

    int insertSelective(TiProductshowstyles record);

    TiProductshowstyles selectByPrimaryKey(Integer showstyleid);
    
    List<TiProductshowstyles>selectByAll();

    int updateByPrimaryKeySelective(TiProductshowstyles record);

    int updateByPrimaryKey(TiProductshowstyles record);
}