package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiActivityworks;
import com.bbyiya.vo.calendar.TiActivitysWorkVo;

public interface TiActivityworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiActivityworks record);

    int insertSelective(TiActivityworks record);

    TiActivityworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiActivityworks record);

    int updateByPrimaryKey(TiActivityworks record);
    
    Integer getCountByActStatus(@Param("actid") Integer actid,@Param("status") Integer status);
    
    /**
     * 根据活动ID得到活动制作情况
     * @param actid
     * @param status
     * @param keywords
     * @return
     */
    List<TiActivitysWorkVo>findActWorkListByActId(@Param("actid") Integer actid,@Param("status") Integer status,@Param("keywords") String keywords);
}