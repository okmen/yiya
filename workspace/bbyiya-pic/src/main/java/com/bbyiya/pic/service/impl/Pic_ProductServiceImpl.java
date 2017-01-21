package com.bbyiya.pic.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.pic.service.IPic_ProductService;

@Service("pic_productService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_ProductServiceImpl implements  IPic_ProductService{
	

}
