package com.bbyiya.pic.service;

import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.vo.ReturnModel;


public interface IPic_ProductService {
	
	
	/*---------------------get 获取单个model--------------------------------------------*/
	/**
	 * 获取产品样本详情（旧版）
	 * @param productId
	 * @return
	 */
	ReturnModel getProductSamples(Long productId) ;
	/**
	 * 获取作品样本列表
	 * 2017-02-17 
	 * @param productId
	 * @return
	 */
	ReturnModel getProductSamplelist(Long productId) ;
	/**
	 * 作品详情 - 通过userId，cartId 获取
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfo(Long userId, Long cartId);
	/**
	 * 作品详情 新版 
	 * 2017-6-27
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfoNew(long userId,long cartId);
	/**
	 * 作品详情 - 通过userId，productId 获取编辑作品详情
	 * @param userId
	 * @param productId
	 * @return
	 */
	ReturnModel getMyProductByProductId(Long userId, Long productId);
	/**
	 * 作品详情 -分享页用
	 * @param cartId
	 * @return
	 */
	ReturnModel getMyProductInfo(Long cartId);

	/**
	 * 获取款式图片作品
	 * @param styleId
	 * @return
	 */
	ReturnModel getStyleCoordResult(Long styleId);
	
	/*--------------------------保存、修改操作-------------------------------------------*/
	/**
	 * 保存用户的作品 save user's product 
	 * @param param
	 * @return
	 */
	ReturnModel saveOrEdit_MyProducts(Long userId, MyProductParam param);
	/**
	 * 修改我的作品(只修改 ，不做新增)
	 * @param userId
	 * @param param
	 * @return
	 */
	ReturnModel Edit_MyProducts(Long userId, MyProductParam param);
	/**
	 * 编辑我的作品（新增，修改）
	 * 
	 * 2017-5-10
	 * zy
	 * @param userId
	 * @param param
	 * @return
	 */
	ReturnModel Modify_MyProducts(Long userId, MyProductParam param);
	
	/*---------------------------------find 列表 查询操作----------------------------------------------------*/
	/**
	 * 我的作品列表
	 * @param userId
	 * @return
	 */
	ReturnModel findMyProlist(Long userId);
	/**
	 * 影楼的作品列表
	 * @param branchUserId
	 * @param status
	 * @param inviteStatus
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyProductsForBranch(Long branchUserId,Integer status,Integer inviteStatus,String keywords, int index,int size);
	
	/*-------------------------------delete 删除操作------------------------------------------------------*/
	/**
	 * 删除我的作品图片
	 * @param userId
	 * @param dpId
	 * @return
	 */
	ReturnModel del_myProductDetail(Long userId, Long dpId);
	/**
	 * 根据cartId 删除我的作品
	 * @param userId
	 * @param cartId
	 * @return
	 */
	ReturnModel deleMyProduct(Long userId, Long cartId);
	/**
	 * 得到模板下的作品列表
	 * @param branchUserId
	 * @param tempid
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyProductslistForTempId(Long branchUserId, Integer tempid,
			Integer activeStatus,String keywords,int index, int size);
	
}
