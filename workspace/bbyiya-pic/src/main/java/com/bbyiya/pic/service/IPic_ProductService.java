package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.vo.ReturnModel;


public interface IPic_ProductService {
	
	/**
	 * ��ȡ��Ʒ��������
	 * @param productId
	 * @return
	 */
	ReturnModel getProductSamples(Long productId) ;
	/**
	 * �����û�����Ʒ save user's product 
	 * @param param
	 * @return
	 */
	ReturnModel saveOrEdit_MyProducts(Long userId, MyProductParam param);
}
