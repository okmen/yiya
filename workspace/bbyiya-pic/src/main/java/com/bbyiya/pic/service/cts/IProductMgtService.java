package com.bbyiya.pic.service.cts;

import com.bbyiya.dto.PProductsDTO;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.ProductSearchParam;



public interface IProductMgtService {
	
	/**
	 * 根据查询条件获取产品列表
	 * @param index
	 * @param size
	 * @param searchParam
	 * @return
	 */
	ReturnModel findProductListBySearchParam(int index, int size,
			ProductSearchParam searchParam);
	/**
	 * 根据产品ID修改产品信息
	 * @param pdto
	 * @return
	 */
	ReturnModel updateProductByProductId(PProductsDTO pdto);
	/**
	 * 根据查询条件获取产品款式列表
	 * @param index
	 * @param size
	 * @param searchParam
	 * @return
	 */
	ReturnModel findProductStylesBySearchParam(int index, int size,
			ProductSearchParam searchParam);
	/**
	 * 新增修改产品款式
	 * @param styles
	 * @return
	 */
	ReturnModel addAndupdateProductStyles(Long userId,PProductstyles styles);
	
	
	
	
}
