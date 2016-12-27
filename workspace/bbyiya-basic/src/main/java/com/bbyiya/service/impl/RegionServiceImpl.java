package com.bbyiya.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.service.IRegionService;

@Service("regionServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class RegionServiceImpl implements IRegionService{
	
}
