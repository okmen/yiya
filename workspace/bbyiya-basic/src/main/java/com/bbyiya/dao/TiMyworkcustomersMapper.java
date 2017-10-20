package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiMyworkcustomers;

public interface TiMyworkcustomersMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiMyworkcustomers record);

    int insertSelective(TiMyworkcustomers record);

    TiMyworkcustomers selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiMyworkcustomers record);

    int updateByPrimaryKey(TiMyworkcustomers record);
    
    /**
     * 根据活动参与者ID得到代客户制作列表
     * @param promoteruserid
     * @return
     */
    List<TiMyworkcustomers>selectListByPromoterUserId(@Param("promoteruserid") Long promoteruserid,@Param("keywords") String keywords);
}