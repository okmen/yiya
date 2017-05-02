package com.bbyiya.pic.web.user;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.pic.service.IPic_myProductService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user/myProduct")
public class MyProductMgtController extends SSOController {
	@Resource(name = "pic_myProductService")
	private IPic_myProductService myproductService;

	@Autowired
	private PMyproductsMapper myproductMapper;
	
	
	@ResponseBody
	@RequestMapping(value = "/mylist")
	public String myProlist(@RequestParam(required = false, defaultValue = "1") int index,@RequestParam(required = false, defaultValue = "10") int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = myproductService.find_mycarts(user.getUserId(),user.getMobilePhone(), index, size);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getmyproduct")
	public String getmyproduct(long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) { 
			rq = myproductService.get_mycart(user.getUserId(),cartId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取作品二维码分享版本号
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getVersionNo")
	public String getVersionNo(long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) { 
			PMyproducts mycart= myproductMapper.selectByPrimaryKey(cartId);
			if(mycart!=null&&mycart.getUserid().longValue()==user.getUserId()){
				String versionString=DateUtil.getTimeStr(new Date(), "yyyyMMddHHMMss"); 
				mycart.setVersion(versionString);
				myproductMapper.updateByPrimaryKeySelective(mycart);
				rq.setStatu(ReturnStatus.Success);
				rq.setBasemodle(versionString);
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("非本人作品！"); 
			} 
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
