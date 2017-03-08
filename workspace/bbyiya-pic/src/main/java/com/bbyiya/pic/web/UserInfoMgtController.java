package com.bbyiya.pic.web;


import javax.annotation.Resource;







import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.pic.service.IPic_UserMgtService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/user")
public class UserInfoMgtController extends SSOController {
	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "baseUserAddressServiceImpl")
	private IBaseUserAddressService addressService;
	@Autowired
	private UUseraddressMapper userAddressMapper;
	@Autowired
	private EErrorsMapper logger;
	/**
	 * 登陆、注册 service
	 */
	@Resource(name = "pic_userMgtService")
	private IPic_UserMgtService loginService;
	/**
	 * A02 新增/编辑 收货地址
	 * 
	 * @param addrJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrEditAddr")
	public String area(String addrJson) throws Exception {
		LoginSuccessResult user = super.getLoginUser();
		ReturnModel rq = new ReturnModel();
		if (user != null) {
			UUseraddress address =  Json2Objects.getParam_UUseraddress(addrJson); //(UUseraddress) JsonUtil.jsonStrToObject(addrJson, UUseraddress.class);
			if (address != null) {
				address.setUserid(user.getUserId());
				rq = addressService.addOrEdit_UserAddressReturnAddressId(address);
				if(rq.getStatu().equals(ReturnStatus.Success)){
					rq.setBasemodle(addressService.getUserAddressResult(user.getUserId(), (Long)rq.getBasemodle()));
				}
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误");
				logger.addError(this.getClass().getName(), "A02 参数错误:Param:"+addrJson);
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取用户地址信息
	 * @param addrid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserAddress")
	public String getUserAddress(@RequestParam(required = false, defaultValue = "0") long addrid) throws Exception {
		LoginSuccessResult user = super.getLoginUser();
		ReturnModel rq = new ReturnModel();
		if (user != null) {
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(addressService.getUserAddressResult(user.getUserId(),addrid)); 		
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 绑定手机
	 * @param vcode
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setPwd")
	public String setPwd(String vcode ,String phone,String pwd) throws Exception {
		LoginSuccessResult user = super.getLoginUser();
		ReturnModel rq = new ReturnModel();
		if (user != null) {
			rq=loginService.setPwd(user.getUserId(), pwd, phone, vcode);
			if(rq.getStatu().equals(ReturnStatus.Success)){
				String ticket= super.getTicket();// CookieUtils.getCookieBySessionId(request);
				if(ObjectUtil.isEmpty(ticket)){
					ticket=CookieUtils.getCookieBySessionId(request);
				}
				if(!ObjectUtil.isEmpty(ticket)){
					 RedisUtil.setObject(ticket, rq.getBasemodle(), 86400); 
					 rq.setStatusreson("设置成功！");
					 return JsonUtil.objectToJsonStr(rq); 
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
