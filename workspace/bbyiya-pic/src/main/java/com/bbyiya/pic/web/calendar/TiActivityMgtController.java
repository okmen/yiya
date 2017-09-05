package com.bbyiya.pic.web.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_act")
public class TiActivityMgtController extends SSOController {
	@Autowired
	private TiActivityworksMapper activityworksMapper;
	@Autowired
	private TiActivitysMapper actMapper;
	
	
	
}
