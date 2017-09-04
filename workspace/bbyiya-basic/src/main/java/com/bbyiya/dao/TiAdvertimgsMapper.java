package com.bbyiya.dao;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiAdvertimgs;

public interface TiAdvertimgsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiAdvertimgs record);

    int insertSelective(TiAdvertimgs record);

    TiAdvertimgs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiAdvertimgs record);

    int updateByPrimaryKey(TiAdvertimgs record);
    
    /**
     * 根据参数productid和promoterid得到对象
     * @param productid
     * @param promoterid
     * @return
     */
    TiAdvertimgs getAdvertByProductIdAndPromoterId(@Param("productid") Long productid,@Param("promoterid") Long promoterid);
}