package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiProductareas;

public interface TiProductareasMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TiProductareas record);

    int insertSelective(TiProductareas record);

    TiProductareas selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TiProductareas record);

    int updateByPrimaryKey(TiProductareas record);
    
    List<TiProductareas>findProductCannotSetAreas(@Param("productid") Long productid,@Param("produceruserid") Long produceruserid);
    
    TiProductareas getProductAreaByIds(@Param("productid") Long productid,@Param("produceruserid") Long produceruserid,@Param("areacode") Integer areacode);
    
    TiProductareas getIfExistProductAreaByOtherIds (@Param("productid") Long productid,@Param("produceruserid") Long produceruserid,@Param("areacode") Integer areacode);
}