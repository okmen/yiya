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
//import com.bbyiya.model.PProductstyleproperty;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.PScenebacks;
import com.bbyiya.model.PStylebackgrounds;
import com.bbyiya.service.pic.IBaseOrderMgtService;
import com.bbyiya.service.pic.IBaseProductService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.PProductStyleResult;
import com.bbyiya.vo.product.ProductResult;
//import com.bbyiya.vo.product.ProductStandardResult;
//import com.bbyiya.vo.product.SecondStandard;

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
	private PStylebackgroundsMapper stylebackgroundsMapper;//款式制作背景图
	@Autowired
	private PScenebacksMapper scenebacksMapper;//场景背面图文
	
	@Resource(name="baseOrderMgtServiceImpl")
	private IBaseOrderMgtService orderMgtService;

	public List<ProductResult> findProductList(Long userId) {
		List<ProductResult> results = productsMapper.findProductResultByBranchUserId(userId);
		if (results != null && results.size() > 0) {
			List<Map<String, String>> imgList = ConfigUtil.getMaplist("productDesimgs");
			if (imgList != null && imgList.size() > 0) {
				for (ProductResult pro : results) {
					List<String> imgs = new ArrayList<String>();
					for (Map<String, String> map : imgList) {
						if (String.valueOf(pro.getProductId()).equals(map.get("productId"))) {
							imgs.add(map.get("imageUrl"));
						}
					}
					pro.setDesImgs(imgs);
				}
			}
		}
		return results;
	}

	//获取产品详情（包括各种款式属性）
	public ProductResult getProductResult(Long productId) {
		ProductResult result = productsMapper.getProductResultByProductId(productId);
		List<PProductStyleResult> styleList = styleMapper.findStylesResultByProductId(productId);
		if(styleList!=null){
			for (PProductStyleResult style : styleList) {
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
//		List<ProductStandardResult> propertyList = standardsMapper.findStandardResult(1l);
//		List<ProductStandardResult> propertytWOList = standardsMapper.findStandardResult(2l);
//		List<PProductstyleproperty> productProperyList = propertyMapper.findStylePropertyByProductId(productId);
//		if (propertyList != null && propertyList.size() > 0) {
//			for (ProductStandardResult ss : propertyList) {
////				ss = getStandardResult(ss, productProperyList, propertytWOList, styleList);
//			}
//		}
//		result.setPropertyList(propertyList);
		return result;
	}

//	public ProductStandardResult getStandardResult(ProductStandardResult result, List<PProductstyleproperty> productProperyList, List<ProductStandardResult> propertytWOList, List<PProductstyles> styleList) {
//		// 二级属性
//		Map<Long, Integer> map = new HashMap<Long, Integer>();
//		boolean isOk = false;
//		for (PProductstyleproperty pro : productProperyList) {
//			if (result.getStandardId().longValue() == pro.getStandardvalueid().longValue()) {
//				isOk = true;
//				if (!map.containsKey(pro.getStyleid())) {
//					map.put(pro.getStyleid(), 1);
//				}
//			}
//		}
//		if (isOk) {
//			List<SecondStandard> list2 = new ArrayList<SecondStandard>();
//			for (PProductstyleproperty so : productProperyList) {
//				if (so.getStandardid().longValue() == 2l && map.containsKey(so.getStyleid())) {
//					SecondStandard ssStandard = new SecondStandard();
//					ssStandard.setStandardId(so.getStandardvalueid());
//					for (ProductStandardResult secondStandard : propertytWOList) {
//						if (secondStandard.getStandardId().longValue() == so.getStandardvalueid()) {
//							ssStandard.setStandardname(secondStandard.getStandardName());
//						}
//					}
//					for (PProductstyles style : styleList) {
//						if (style.getStyleid().longValue() == so.getStyleid().longValue()) {
//							ssStandard.setPrice(style.getPrice());
//							ssStandard.setStyleId(style.getStyleid());
//							List<PProductstyledes> desImgs = styleDesMapper.findImgsByStyleId(style.getStyleid());
//							if (desImgs != null && desImgs.size() > 0) {
//								List<String> imgsList = new ArrayList<String>();
//								for (PProductstyledes ss : desImgs) {
//									imgsList.add(ss.getImgurl());
//								}
//								ssStandard.setDetailImgs(imgsList);
//							}
//						}
//					}
//					list2.add(ssStandard);
//				}
//			}
//			result.setSubList(list2);
//		}
//		return result;
//	}
	
	 
	
	public ReturnModel getStyleInfo(Long styleId){
		ReturnModel rq=new ReturnModel();
		PProductstyles style= styleMapper.selectByPrimaryKey(styleId);
		if(style!=null){
			PProducts product= productsMapper.selectByPrimaryKey(style.getProductid());
			if(product!=null){
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("title", product.getTitle());
				map.put("branchUserId", product.getUserid());
				map.put("defaultImg", product.getDefaultimg());
				map.put("price", style.getPrice());
				map.put("styleId", styleId);
				map.put("productId", style.getProductid());
				map.put("propertystr", orderMgtService.getStylePropertyStr(styleId));
				rq.setBasemodle(map);
				rq.setStatu(ReturnStatus.Success); 
			}
		}
		return rq;
	}
	
	public ReturnModel find_previewsImg(long styleId,Integer[] ids){
		ReturnModel rq=new ReturnModel();
		//根据款式获取制作背景图 
		List<PStylebackgrounds> backgrounds=stylebackgroundsMapper.findBacksByStyleId(styleId);
		
		//获取场景背面图文
		Map<String, Object> mapParam=new HashMap<String, Object>();
		mapParam.put("styleId", styleId);
		mapParam.put("ids", ids);
		List<PScenebacks> backsList= scenebacksMapper.findScenelistByMap(mapParam);
		
		//场景背面图返回结果集
		List<Map<String, Object>> scenenbacksMaps=new ArrayList<Map<String,Object>>();
		for (Integer id : ids) {
			Map<String, Object> backMap=new HashMap<String, Object>();
			for (PScenebacks bb : backsList) {
				if(id.intValue()==bb.getSceneid().intValue()){
					backMap.put("sceneId", id);
					backMap.put("imageUrl", bb.getImageurl());
					scenenbacksMaps.add(backMap);
				}
			}
		}
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("backgrounds", backgrounds);
		map.put("sceneBacks", scenenbacksMaps);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	public ReturnModel find_previewsImg(long styleId){
		ReturnModel rq=new ReturnModel();
		//根据款式获取制作背景图 
		List<PStylebackgrounds> backgrounds=stylebackgroundsMapper.findBacksByStyleId(styleId);
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("backgrounds", backgrounds);
		rq.setBasemodle(map);
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}

}
