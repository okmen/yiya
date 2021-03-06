package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstyledesMapper;
import com.bbyiya.dao.PProductstylepropertyMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.PScenebacksMapper;
import com.bbyiya.dao.PStandardsMapper;
import com.bbyiya.dao.PStylebackgroundsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyledes;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.PScenebacks;
import com.bbyiya.model.PStylebackgrounds;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.service.pic.IBaseProductService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.PProductStyleResult;
import com.bbyiya.vo.product.ProductResult;

@SuppressWarnings("restriction")
@Service("baseProductServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseProductServiceImpl implements IBaseProductService {
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PStandardsMapper standardsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	@Autowired
	private PProductstylepropertyMapper propertyMapper;
	@Autowired
	private PProductstyledesMapper styleDesMapper;
	@Autowired
	private PStylebackgroundsMapper stylebackgroundsMapper;// 款式制作背景图
	@Autowired
	private PScenebacksMapper scenebacksMapper;// 场景背面图文

	@Resource(name = "baseOrderMgtServiceImpl")
	private IBaseOrderMgtService orderMgtService;

	public List<ProductResult> findProductList(Long userId) {
		List<ProductResult> results = productsMapper.findProductResultByBranchUserId(userId);
		return results;
	}

	// 获取产品详情（包括各种款式属性）
	public ProductResult getProductResult(Long productId) {
		ProductResult result = productsMapper.getProductResultByProductId(productId);
		List<PProductStyleResult> styleList = styleMapper.findStylesResultByProductId(productId);
		if (result!=null&& styleList != null) {
			for (PProductStyleResult style : styleList) {
				//款式介绍图集
				List<PProductstyledes> desImgs = styleDesMapper.findImgsByStyleId(style.getStyleId());
				if (desImgs != null && desImgs.size() > 0) {
					List<String> imgsList = new ArrayList<String>();
					for (PProductstyledes ss : desImgs) {
						imgsList.add(ss.getImgurl());
					}
					style.setDetailImgs(imgsList);
				}
			}
			result.setStyleslist(styleList);
		}
		return result;
	}

	public ReturnModel getStyleInfo(Long styleId) {
		ReturnModel rq = new ReturnModel();
		PProductstyles style = styleMapper.selectByPrimaryKey(styleId);
		if (style != null) {
			PProducts product = productsMapper.selectByPrimaryKey(style.getProductid());
			if (product != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("title", product.getTitle());
				map.put("branchUserId", product.getUserid());
				map.put("defaultImg", product.getDefaultimg());
				map.put("price", style.getPrice());
				map.put("styleId", styleId);
				map.put("productId", style.getProductid());
				map.put("propertystr", style.getPropertystr());//orderMgtService.getStylePropertyStr(styleId)
				rq.setBasemodle(map);
				rq.setStatu(ReturnStatus.Success);
			}
		}
		return rq;
	}

	public ReturnModel find_previewsImg(long styleId, Integer[] ids) {
		ReturnModel rq = new ReturnModel();
		// 根据款式获取制作背景图
		List<PStylebackgrounds> backgrounds = stylebackgroundsMapper.findBacksByStyleId(styleId);

		// 获取场景背面图文
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("styleId", styleId);
		mapParam.put("ids", ids);
		List<PScenebacks> backsList = scenebacksMapper.findScenelistByMap(mapParam);

		// 场景背面图返回结果集
		List<Map<String, Object>> scenenbacksMaps = new ArrayList<Map<String, Object>>();
		for (Integer id : ids) {
			Map<String, Object> backMap = new HashMap<String, Object>();
			for (PScenebacks bb : backsList) {
				if (id.intValue() == bb.getSceneid().intValue()) {
					backMap.put("sceneId", id);
					backMap.put("imageUrl", bb.getImageurl());
					scenenbacksMaps.add(backMap);
				}
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("backgrounds", backgrounds);
		map.put("sceneBacks", scenenbacksMaps);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

	public ReturnModel find_previewsImg(long styleId) {
		ReturnModel rq = new ReturnModel();
		// 根据款式获取制作  正面的背景图
		List<PStylebackgrounds> backgrounds = stylebackgroundsMapper.findBacksByStyleId(styleId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("backgrounds", backgrounds);
		//背面的背景图
		List<Map<String, String>> mapList=ConfigUtil.getMaplist("stylebacks");
		if(mapList!=null&&mapList.size()>0){
			for (Map<String, String> mapStyle : mapList) {
				if(styleId==ObjectUtil.parseLong(mapStyle.get("styleId"))){
					map.put("back", mapStyle.get("backurl"));
				}
			}
		}
		List<Map<String, String>> covers=ConfigUtil.getMaplist("styleCovers");
		for (Map<String, String> cover : covers) {
			if(styleId==ObjectUtil.parseLong(cover.get("styleId"))){
				map.put("cover", cover.get("url"));
			}
		}
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

}
