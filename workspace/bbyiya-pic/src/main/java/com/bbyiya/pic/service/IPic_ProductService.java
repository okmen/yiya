package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.vo.ReturnModel;


public interface IPic_ProductService {
	
	
	/*---------------------get ��ȡ����model--------------------------------------------*/
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
	 * ��ȡ��ʽͼƬ��Ʒ
	 * @param styleId
	 * @return
	 */
	ReturnModel getStyleCoordResult(Long styleId);
	
	/*--------------------------���桢�޸Ĳ���-------------------------------------------*/
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
	 * �༭�ҵ���Ʒ���������޸ģ�
	 * 
	 * 2017-5-10
	 * zy
	 * @param userId
	 * @param param
	 * @return
	 */
	ReturnModel Modify_MyProducts(Long userId, MyProductParam param);
	
	/*---------------------------------find �б� ��ѯ����----------------------------------------------------*/
	/**
	 * �ҵ���Ʒ�б�
	 * @param userId
	 * @return
	 */
	ReturnModel findMyProlist(Long userId);
	/**
	 * Ӱ¥����Ʒ�б�
	 * @param branchUserId
	 * @param status
	 * @param inviteStatus
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyProductsForBranch(Long branchUserId,Integer status,Integer inviteStatus, int index,int size);
	
	/*-------------------------------delete ɾ������------------------------------------------------------*/
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
	 * �õ�ģ���µ���Ʒ�б�
	 * @param branchUserId
	 * @param tempid
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyProductslistForTempId(Long branchUserId, Integer tempid,
			Integer activeStatus,String keywords,int index, int size);
	
}
