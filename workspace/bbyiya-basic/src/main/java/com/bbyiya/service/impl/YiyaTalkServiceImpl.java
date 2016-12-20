package com.bbyiya.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.service.IYiyaTalkService;
import com.bbyiya.vo.talks.YiyaTalkBannerModel;

@Service("yiyaTalkService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class YiyaTalkServiceImpl implements IYiyaTalkService{
	
	public List<YiyaTalkBannerModel> find_talkBanners(){
		return null;
	}
}
