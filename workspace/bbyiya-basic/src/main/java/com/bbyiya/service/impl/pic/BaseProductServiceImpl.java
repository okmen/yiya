package com.bbyiya.service.impl.pic;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.service.pic.IBaseProductService;

@Service("baseProductServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class BaseProductServiceImpl implements IBaseProductService{

	
	
}
