package com.bbyiya.dao;

import com.bbyiya.model.TiMyworkcustomers;

public interface TiMyworkcustomersMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiMyworkcustomers record);

    int insertSelective(TiMyworkcustomers record);

    TiMyworkcustomers selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiMyworkcustomers record);

    int updateByPrimaryKey(TiMyworkcustomers record);
}