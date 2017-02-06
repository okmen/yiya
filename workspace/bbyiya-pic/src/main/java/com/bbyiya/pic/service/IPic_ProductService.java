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
	/**
	 * �ҵ���Ʒ�б�
	 * @param userId
	 * @return
	 */
	ReturnModel findMyProlist(Long userId);
	/**
	 * ��ȡ��Ʒ����
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfo(Long userId, Long cartId);
	/**
	 * ��Ʒ����
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfo(Long cartId);
	/**
	 * ɾ���ҵ���ƷͼƬ
	 * @param userId
	 * @param dpId
	 * @return
	 */
	ReturnModel del_myProductDetail(Long userId, Long dpId);
	
	/**
	 * 
	 * @param styleId
	 * @return
	 */
	ReturnModel getStyleCoordResult(Long styleId);
}
