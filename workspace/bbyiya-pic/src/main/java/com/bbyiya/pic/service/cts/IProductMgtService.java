package com.bbyiya.pic.service.cts;

import com.bbyiya.dto.PProductsDTO;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.ProductSearchParam;



public interface IProductMgtService {
	
	/**
	 * ���ݲ�ѯ������ȡ��Ʒ�б�
	 * @param index
	 * @param size
	 * @param searchParam
	 * @return
	 */
	ReturnModel findProductListBySearchParam(int index, int size,
			ProductSearchParam searchParam);
	/**
	 * ���ݲ�ƷID�޸Ĳ�Ʒ��Ϣ
	 * @param pdto
	 * @return
	 */
	ReturnModel updateProductByProductId(PProductsDTO pdto);
	/**
	 * ���ݲ�ѯ������ȡ��Ʒ��ʽ�б�
	 * @param index
	 * @param size
	 * @param searchParam
	 * @return
	 */
	ReturnModel findProductStylesBySearchParam(int index, int size,
			ProductSearchParam searchParam);
	/**
	 * �����޸Ĳ�Ʒ��ʽ
	 * @param styles
	 * @return
	 */
	ReturnModel addAndupdateProductStyles(Long userId,PProductstyles styles);
	
	
	
	
}
