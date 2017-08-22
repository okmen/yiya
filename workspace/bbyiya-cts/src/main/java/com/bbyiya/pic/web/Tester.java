package com.bbyiya.pic.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.dao.IPic_DataTempDao;
import com.bbyiya.pic.vo.AgentDateVO;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/test")
public class Tester extends SSOController{
	@Autowired
	private IPic_DataTempDao temCtsDao;

	@ResponseBody 
	@RequestMapping(value = "/randUuid")
	public String templateMessageSend(int  type) throws Exception {
		ReturnModel rq = new ReturnModel();
		List<AgentDateVO> list= temCtsDao.findActslist(type,null);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(list);
		return JsonUtil.objectToJsonStr(rq);
	
	}
}
