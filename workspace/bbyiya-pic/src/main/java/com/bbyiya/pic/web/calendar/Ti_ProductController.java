package com.bbyiya.pic.web.calendar;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiUserdiscountsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_product")
public class Ti_ProductController  extends SSOController {
	@Autowired
	private TiProductsMapper productsMapper;
	@Autowired
	private TiUserdiscountsMapper mydiscountMapper;
	
	/**
	 * 挂历列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/prolist")
	public String transferPage() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq.setBasemodle(productsMapper.findProductlist());
			rq.setStatu(ReturnStatus.Success);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
