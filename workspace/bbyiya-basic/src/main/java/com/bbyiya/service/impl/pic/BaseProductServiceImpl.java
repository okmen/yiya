package com.bbyiya.service.impl.pic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.service.pic.IBaseProductService;
import com.bbyiya.vo.product.ProductResult;

@Service("baseProductServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseProductServiceImpl implements IBaseProductService{
	@Autowired
	private PProductsMapper productsMapper;
	
	public List<ProductResult> findProductList(Long userId){
		return productsMapper.findProductResultByBranchUserId(userId);
	}
}
