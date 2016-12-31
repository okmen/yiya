package com.bbyiya.service.impl.pic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylepropertyMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.PStandardsMapper;
import com.bbyiya.model.PProductstyleproperty;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.service.pic.IBaseProductService;
import com.bbyiya.vo.product.ProductResult;
import com.bbyiya.vo.product.ProductStandardResult;
import com.bbyiya.vo.product.SecondStandard;

@Service("baseProductServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseProductServiceImpl implements IBaseProductService{
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PStandardsMapper standardsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	@Autowired
	private PProductstylepropertyMapper propertyMapper;
	
	public List<ProductResult> findProductList(Long userId){
		return productsMapper.findProductResultByBranchUserId(userId);
	}
	
	public ProductResult getProductResult(Long productId){
		ProductResult result= productsMapper.getProductResultByProductId(productId);
		List<PProductstyles> styleList=styleMapper.findStylesByProductId(productId);
		List<ProductStandardResult> propertyList= standardsMapper.findStandardResult(1l);
		List<ProductStandardResult> propertytWOList= standardsMapper.findStandardResult(2l);
		List<PProductstyleproperty> productProperyList=propertyMapper.findStylePropertyByProductId(productId);
		if(propertyList!=null&&propertyList.size()>0){
			for (ProductStandardResult ss : propertyList) {
				ss=getStandardResult(ss, productProperyList,propertytWOList, styleList);
			}
		}
		result.setPropertyList(propertyList); 
		return result;
	}
	
	public ProductStandardResult getStandardResult(ProductStandardResult result,List<PProductstyleproperty> productProperyList,List<ProductStandardResult> propertytWOList,List<PProductstyles> styleList){
		//二级属性
		Map<Long, Integer> map=new HashMap<Long, Integer>();
		boolean isOk=false;
		for (PProductstyleproperty pro : productProperyList) {
			if(result.getStandardId().longValue()==pro.getStandardid().longValue()){
				isOk=true;
				if(!map.containsKey(pro.getStyleid())){
					map.put(pro.getStyleid(), 1);
				}
			}
		}
		if(isOk){
			List<SecondStandard> list2=new ArrayList<SecondStandard>();
			for (PProductstyleproperty so : productProperyList) {
				if(so.getStandardid().longValue()==2l&&map.containsKey(so.getStyleid())){
					SecondStandard ssStandard=new  SecondStandard();
					ssStandard.setStandardId(so.getStandardvalueid());
					for (ProductStandardResult secondStandard : propertytWOList) {
						if(secondStandard.getStandardId().longValue()==so.getStandardvalueid()){
							ssStandard.setStandardname(secondStandard.getStandardName()); 
						}
					}
					for (PProductstyles style : styleList) {
						if(style.getStyleid().longValue()==so.getStyleid().longValue()){
							ssStandard.setPrice(style.getPrice());
							ssStandard.setStyleId(style.getStyleid()); 
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
