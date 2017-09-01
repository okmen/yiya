package com.bbyiya.pic.web.calendar;


import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.pic.service.calendar.IIbs_TiPromoterEmployeeService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiEmployeeActOffVo;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/tiemployee")
public class TiPromoterEmployeeController extends SSOController {
	
	@Resource(name = "ibs_TiPromoterEmployeeService")
	private IIbs_TiPromoterEmployeeService employeeService;
	@Autowired
	private UUsersMapper userMapper;
	
	/**
	 * 内部账户管理
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/employeelist")
	public String employeelist(int index,int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=employeeService.findPromoterEmployeelistByPromoterId(user.getUserId(),index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 新增员工信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addEmployee")
	public String addEmployee(String memberJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiEmployeeActOffVo param= (TiEmployeeActOffVo) JsonUtil.jsonStrToObject(memberJson, TiEmployeeActOffVo.class);
			rq=employeeService.addEmployeeUser(user.getUserId(), param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 移除员工
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delEmployee")
	public String delEmployee(Long  userId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=employeeService.delEmployeeUser(user.getUserId(), userId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
}
