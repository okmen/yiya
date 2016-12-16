package com.bbyiya.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.service.IBigCaseService;

@Service("bigCaseService")
@Transactional(rollbackFor={RuntimeException.class, Exception.class})
public class BigCaseServiceImpl implements IBigCaseService{

	
}
