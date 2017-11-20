package com.bbyiya.service.pic;

import java.util.List;

import com.bbyiya.model.PPostmodel;
import com.bbyiya.vo.ReturnModel;

public interface IBasePostMgtService {

	/**
	 * 下单页用-根据收货地址获取邮费信息 zy v1.0
	 * 
	 * @param addressId
	 * @return
	 */
	ReturnModel find_postagelist(Long addressId);

	/**
	 * 产品的运费模块列表
	 * @param addressId
	 * @param productId
	 * @return
	 */
	ReturnModel find_postagelist(Long addressId, Long productId);
	/**
	 * 产品运费模块
	 * @param area
	 * @param productId
	 * @return
	 */
	 ReturnModel find_postlist(Integer area,Long productId);

	/**
	 * 列出快递方式 以及快递费（供用户选择） zy（17-03-30）
	 * 
	 * @param area
	 *            （收货区域）
	 * @return
	 */
	List<PPostmodel> find_postlist(Integer area);

	/**
	 * 获取某个产品的运费方式
	 * 
	 * @param area
	 * @param productId
	 * @return
	 */
	ReturnModel find_postlist_ti(Integer area, Long productId);
	
	ReturnModel find_postlist_ti(Long addressId,Long productId) ;

	/**
	 * 通过 快递方式Id,areaId 确定快递费 zy（17-03-30）
	 * 
	 * @param postModelId
	 *            快递方式
	 * @param areaId
	 *            地区id
	 * @return
	 */
	PPostmodel getPostmodel(Integer postModelId, Integer areaId);

	/**
	 * 新增运费模板 julie(17-04-17)
	 * 
	 * @param userid
	 * @param name
	 * @param amount
	 */
	ReturnModel addPostmodel(Long userid, String name, Double amount);

	/**
	 * 修改运费模板 julie(17-04-17)
	 * 
	 * @param postModelId
	 * @param name
	 * @param amount
	 */
	ReturnModel editPostmodel(int postModelId, String name, Double amount);

	/**
	 * 获取邮费模板特殊区域列表
	 * 
	 * @author julie
	 * @param index
	 * @param size
	 * @param areacode
	 * @param areaname
	 * @return
	 */
	ReturnModel find_PostModelAreaslist(int index, int size, Integer areacode, String areaname);

	/**
	 * 新增邮费模板特殊区域
	 * 
	 * @author julie
	 * @param postModelJson
	 * @return
	 */
	ReturnModel addPostModelAreas(String postModelJson);

	/**
	 * 修改邮费模板特殊区域
	 * 
	 * @author julie
	 * @param postModelJson
	 * @return
	 */
	ReturnModel editPostModelAreas(String postModelJson);

	/**
	 * 删除邮费模板特殊区域
	 * 
	 * @author julie
	 * @param postId
	 * @return
	 */
	ReturnModel delPostModelAreas(Integer postId);

	/**
	 * 配置文件中读取物流公司信息
	 * 
	 * @return
	 */
	ReturnModel getPostInfo();
	
	Double getPostAge_ti(Long addressId,Long productId);
}
