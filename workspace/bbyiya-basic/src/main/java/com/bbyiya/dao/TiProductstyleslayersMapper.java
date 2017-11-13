package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiProductstyleslayers;
import com.bbyiya.vo.calendar.product.TiStyleLayerResult;

public interface TiProductstyleslayersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiProductstyleslayers record);

    int insertSelective(TiProductstyleslayers record);

    TiProductstyleslayers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiProductstyleslayers record);

    int updateByPrimaryKey(TiProductstyleslayers record);
    /**
     * 获取款式图层列表
     * @param styleId
     * @return
     */
    List<TiStyleLayerResult> findLayerlistByStyleId(@Param("styleId")Long styleId);
}