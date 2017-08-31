package com.bbyiya.pic.web.calendar;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.pic.service.calendar.IIbs_TiPromoterEmployeeService;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/calendar/employee/")
public class TiPromoterEmployeeController extends SSOController {
	
	@Resource(name = "ibs_TiPromoterEmployeeService")
	private IIbs_TiPromoterEmployeeService employeeService;
	@Autowired
	private UUsersMapper userMapper;
	
}
