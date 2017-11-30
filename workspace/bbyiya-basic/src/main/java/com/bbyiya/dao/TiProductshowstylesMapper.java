package com.bbyiya.dao;

import com.bbyiya.model.TiProductshowstyles;

public interface TiProductshowstylesMapper {
    int deleteByPrimaryKey(Integer showstyleid);

    int insert(TiProductshowstyles record);

    int insertSelective(TiProductshowstyles record);

    TiProductshowstyles selectByPrimaryKey(Integer showstyleid);

    int updateByPrimaryKeySelective(TiProductshowstyles record);

    int updateByPrimaryKey(TiProductshowstyles record);
}