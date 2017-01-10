package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstyledesMapper;
import com.bbyiya.dao.PProductstylepropertyMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.PStandardsMapper;
import com.bbyiya.model.PProductstyledes;
import com.bbyiya.model.PProductstyleproperty;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.service.pic.IBaseProductService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.vo.product.ProductResult;
import com.bbyiya.vo.product.ProductStandardResult;
import com.bbyiya.vo.product.SecondStandard;

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

	public ProductResult getProductResult(Long productId) {
		ProductResult result = productsMapper.getProductResultByProductId(productId);
		List<PProductstyles> styleList = styleMapper.findStylesByProductId(productId);
		List<ProductStandardResult> propertyList = standardsMapper.findStandardResult(1l);
		List<ProductStandardResult> propertytWOList = standardsMapper.findStandardResult(2l);
		List<PProductstyleproperty> productProperyList = propertyMapper.findStylePropertyByProductId(productId);
		if (propertyList != null && propertyList.size() > 0) {
			for (ProductStandardResult ss : propertyList) {
				ss = getStandardResult(ss, productProperyList, propertytWOList, styleList);
			}
		}
		result.setPropertyList(propertyList);
		return result;
	}

	public ProductStandardResult getStandardResult(ProductStandardResult result, List<PProductstyleproperty> productProperyList, List<ProductStandardResult> propertytWOList, List<PProductstyles> styleList) {
		// 二级属性
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		boolean isOk = false;
		for (PProductstyleproperty pro : productProperyList) {
			if (result.getStandardId().longValue() == pro.getStandardid().longValue()) {
				isOk = true;
				if (!map.containsKey(pro.getStyleid())) {
					map.put(pro.getStyleid(), 1);
				}
				List<Map<String, String>> imgList = ConfigUtil.getMaplist("backgroundimgs");
				if (imgList != null && imgList.size() > 0) {
					List<String> imgs = new ArrayList<String>();
					for (Map<String, String> mapS : imgList) {
						imgs.add(mapS.get("imageUrl"));
					}
					result.setBackgroundImgs(imgs);

				}
				// result.setBackgroundImg();
			}
		}
		if (isOk) {
			List<SecondStandard> list2 = new ArrayList<SecondStandard>();
			for (PProductstyleproperty so : productProperyList) {
				if (so.getStandardid().longValue() == 2l && map.containsKey(so.getStyleid())) {
					SecondStandard ssStandard = new SecondStandard();
					ssStandard.setStandardId(so.getStandardvalueid());
					for (ProductStandardResult secondStandard : propertytWOList) {
						if (secondStandard.getStandardId().longValue() == so.getStandardvalueid()) {
							ssStandard.setStandardname(secondStandard.getStandardName());
						}
					}
					for (PProductstyles style : styleList) {
						if (style.getStyleid().longValue() == so.getStyleid().longValue()) {
							ssStandard.setPrice(style.getPrice());
							ssStandard.setStyleId(style.getStyleid());
							List<PProductstyledes> desImgs = styleDesMapper.findImgsByStyleId(style.getStyleid());
							if (desImgs != null && desImgs.size() > 0) {
								List<String> imgsList = new ArrayList<String>();
								for (PProductstyledes ss : desImgs) {
									imgsList.add(ss.getImgurl());
								}
								ssStandard.setDetailImgs(imgsList);
							}
						}
					}
					list2.add(ssStandard);
				}
			}
			result.setSubList(list2);
		}
		return result;
	}

}
