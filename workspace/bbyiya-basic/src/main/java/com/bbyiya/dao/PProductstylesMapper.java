package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.PProductstyles;
import com.bbyiya.vo.product.PProductStyleResult;
import com.bbyiya.vo.product.ProductSearchParam;

public interface PProductstylesMapper {
    int deleteByPrimaryKey(Long styleid);

    int insert(PProductstyles record);

    int insertSelective(PProductstyles record);

    PProductstyles selectByPrimaryKey(Long styleid);

    int updateByPrimaryKeySelective(PProductstyles record);

    int updateByPrimaryKey(PProductstyles record);
    /**
     * 获取款式列表
     * @param productId
     * @return
     */
    List<PProductstyles> findStylesByProductId(Long productId);
    /**
     * 获取款式列表 前端展示用
     * @param productId
     * @return
     */
    List<PProductStyleResult> findStylesResultByProductId(Long productId);
    
    /**
     * 根据查询条件获取款式列表
     * @param productId
     * @return
     */
    List<PProductstyles> findProductStylesBySearchParam(ProductSearchParam searchparam);
}