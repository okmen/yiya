package com.bbyiya.dao;

import com.bbyiya.model.TiProductcomments;

public interface TiProductcommentsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiProductcomments record);

    int insertSelective(TiProductcomments record);

    TiProductcomments selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiProductcomments record);

    int updateByPrimaryKey(TiProductcomments record);
}