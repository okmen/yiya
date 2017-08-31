package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiProductcomments;

public interface TiProductcommentsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TiProductcomments record);

    int insertSelective(TiProductcomments record);

    TiProductcomments selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TiProductcomments record);

    int updateByPrimaryKey(TiProductcomments record);
    /**
     * 产品评论
     * @param productId
     * @return
     */
    List<TiProductcomments> findListByProductId(@Param("productId")Long productId);
}