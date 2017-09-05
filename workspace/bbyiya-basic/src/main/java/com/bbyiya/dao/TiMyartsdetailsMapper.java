package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiMyartsdetails;

public interface TiMyartsdetailsMapper {
    int deleteByPrimaryKey(Long detailid);

    int insert(TiMyartsdetails record);

    int insertSelective(TiMyartsdetails record);

    TiMyartsdetails selectByPrimaryKey(Long detailid);

    int updateByPrimaryKeySelective(TiMyartsdetails record);

    int updateByPrimaryKey(TiMyartsdetails record);
    /**
     * 作品图片
     * @param workId
     * @return
     */
    List<TiMyartsdetails> findDetailsByWorkId(@Param("workId")Long workId);
}