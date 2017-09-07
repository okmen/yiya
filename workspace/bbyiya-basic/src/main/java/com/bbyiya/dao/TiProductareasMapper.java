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
    
    /**
     * 生产商该产品不能设置的区域
     * @param productid
     * @param produceruserid
     * @return
     */
    List<TiProductareas>findProductCannotSetAreas(@Param("productid") Long productid,@Param("produceruserid") Long produceruserid);
    
    /**
     * 生产商该产品的区域设置情况
     * @param productid
     * @param produceruserid
     * @return
     */
    List<TiProductareas>findProductAreasByProducerUserId(@Param("productid") Long productid,@Param("produceruserid") Long produceruserid);
    
    TiProductareas getProductAreaByIds(@Param("productid") Long productid,@Param("produceruserid") Long produceruserid,@Param("areacode") Integer areacode);
    
    TiProductareas getIfExistProductAreaByOtherIds (@Param("productid") Long productid,@Param("produceruserid") Long produceruserid,@Param("areacode") Integer areacode);
    /**
     * 获取产品 区域的生产商
     * @param productId
     * @param areaCode
     * @return
     */
    List<TiProductareas> findProductAreaListByProductIdAndArea(@Param("productId")Long productId,@Param("areaCode")Integer areaCode);
}