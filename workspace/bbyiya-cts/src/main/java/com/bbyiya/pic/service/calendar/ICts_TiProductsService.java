package com.bbyiya.pic.service.calendar;

import java.util.List;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.TiProductshowtemplate;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiProductstyleslayers;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.product.TiProductResult;

public interface ICts_TiProductsService {
	
	/**
	 * 台历挂历产品列表
	 * @return
	 */
	ReturnModel getTiProList();
	/**
	 * 修改产品
	 * @param product
	 * @return
	 */
	ReturnModel editTiproduct(TiProductResult product) throws Exception;
	
	/**
	 * 修改详情图集
	 * @param productid
	 * @param imglist
	 * @return
	 * @throws Exception
	 */
	ReturnModel editDescriptionImglist(Long productid, List<ImageInfo> imglist)
			throws Exception;
	/**
	 * 款式列表
	 * @param productid
	 * @return
	 */
	ReturnModel getproductStyleList(Long productid);
	/**
	 * 编辑款式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel editTiStyle(TiProductstyles param) throws Exception;
	/**
	 * 添加款式
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel addTiStyle(TiProductstyles param) throws Exception;
	/**
	 * 得到款式背景图列表
	 * @param styleid
	 * @return
	 */
	ReturnModel getTiStylesLayersList(Long styleid);
	/**
	 * 新增款式背景图
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel addTiStylesLayers(TiProductstyleslayers param) throws Exception;
	/**
	 * 修改款式背景图
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel editTiStylesLayers(TiProductstyleslayers param)
			throws Exception;
	/**
	 * 删除款式背景图
	 * @param layersid
	 * @return
	 * @throws Exception
	 */
	ReturnModel delTiStylesLayers(Long layersid) throws Exception;
	/**
	 * 设置款式合成坐标
	 * @param imgcoord
	 * @param printcoord
	 * @param fontimgcoord
	 * @return
	 * @throws Exception
	 */
	ReturnModel setStyleCoordinate(Long styleid,PStylecoordinateitem imgcoord,
			PStylecoordinateitem printcoord, PStylecoordinateitem fontimgcoord)
			throws Exception;
	
	/**
	 * 设置款式状态
	 * @param styleid
	 * @param status
	 * @return
	 * @throws Exception
	 */
	ReturnModel setStyleStatus(Long styleid, Integer status) throws Exception;
	/**
	 * 新增或修改分页效果
	 * @param userid
	 * @param param
	 * @return
	 * @throws Exception
	 */
	ReturnModel addOrEditProductPageturn(Long userid,
			TiProductshowtemplate param) throws Exception;
	/**
	 * 产品翻页列表
	 * @param userid
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	ReturnModel getProductShowTempList(Long userid, int index, int size)
			throws Exception;
	
	
}
