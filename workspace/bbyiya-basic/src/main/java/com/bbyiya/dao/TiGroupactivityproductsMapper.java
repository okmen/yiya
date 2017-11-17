package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiGroupactivityproducts;

public interface TiGroupactivityproductsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiGroupactivityproducts record);

    int insertSelective(TiGroupactivityproducts record);

    TiGroupactivityproducts selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiGroupactivityproducts record);

    int updateByPrimaryKey(TiGroupactivityproducts record);
    /**
     * 根据活动ID得到产品集合
     * @param gactid
     * @return
     */
    List<TiGroupactivityproducts> findProductsByGActid(Integer gactid);
} 