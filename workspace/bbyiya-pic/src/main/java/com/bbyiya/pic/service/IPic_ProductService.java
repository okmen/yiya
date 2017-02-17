package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.vo.ReturnModel;


public interface IPic_ProductService {
	
	/**
	 * ��ȡ��Ʒ�������飨�ɰ棩
	 * @param productId
	 * @return
	 */
	ReturnModel getProductSamples(Long productId) ;
	/**
	 * ��ȡ��Ʒ�����б�
	 * 2017-02-17 
	 * @param productId
	 * @return
	 */
	ReturnModel getProductSamplelist(Long productId) ;
	/**
	 * �����û�����Ʒ save user's product 
	 * @param param
	 * @return
	 */
	ReturnModel saveOrEdit_MyProducts(Long userId, MyProductParam param);
	/**
	 * �޸��ҵ���Ʒ(ֻ�޸� ����������)
	 * @param userId
	 * @param param
	 * @return
	 */
	ReturnModel Edit_MyProducts(Long userId, MyProductParam param);
	/**
	 * �ҵ���Ʒ�б�
	 * @param userId
	 * @return
	 */
	ReturnModel findMyProlist(Long userId);
	/**
	 * ��Ʒ���� - ͨ��userId��cartId ��ȡ
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfo(Long userId, Long cartId);
	/**
	 * ��Ʒ���� - ͨ��userId��productId ��ȡ�༭��Ʒ����
	 * @param userId
	 * @param productId
	 * @return
	 */
	ReturnModel getMyProductByProductId(Long userId, Long productId);
	/**
	 * ��Ʒ���� -����ҳ��
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
	 * ����cartId ɾ���ҵ���Ʒ
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel deleMyProduct(Long userId, Long cartId);
	
	/**
	 * ��ȡ��ʽͼƬ��Ʒ
	 * @param styleId
	 * @return
	 */
	ReturnModel getStyleCoordResult(Long styleId);
}
