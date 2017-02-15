package com.bbyiya.pic.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.EErrors;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.UUseraddress;
import com.bbyiya.pic.vo.product.MyProductParam;
//import com.bbyiya.service.IRegionService;
import com.bbyiya.service.pic.IBaseUserAddressService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
//import com.bbyiya.vo.user.UUserAddressResult;
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
			UUseraddress address =  getParams(addrJson); //(UUseraddress) JsonUtil.jsonStrToObject(addrJson, UUseraddress.class);
			if (address != null) {
				address.setUserid(user.getUserId());
				rq = addressService.addOrEdit_UserAddressReturnAddressId(address);
				if(rq.getStatu().equals(ReturnStatus.Success)){
					rq.setBasemodle(addressService.getUserAddressResult(user.getUserId(), (Long)rq.getBasemodle()));
				}
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误");
				EErrors errors=new EErrors();
				errors.setClassname(this.getClass().getName());
				errors.setMsg("A02 参数错误:Param:"+addrJson); 
				logger.insert(errors);
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
	
	
	private UUseraddress getParams(String addrJson) {
		JSONObject model = JSONObject.fromObject(addrJson);
		if (model != null) {
			UUseraddress param = new UUseraddress();
			Long addrId=ObjectUtil.parseLong(String.valueOf(model.get("addrid")));
			if(addrId!=null&&addrId>0){
				param.setAddrid(addrId); 
			}
			String reciver=String.valueOf(model.get("reciver"));
			if(!ObjectUtil.isEmpty(reciver)&&!"null".equals(reciver)){
				param.setReciver(reciver);
			}
			String phone=String.valueOf(model.get("phone"));
			if(!ObjectUtil.isEmpty(phone)&&!"null".equals(phone)){
				param.setPhone(phone);
			}
			Integer province=ObjectUtil.parseInt(String.valueOf(model.get("province")));
			if(province!=null&&province>0){
				param.setProvince(province);
			}
			Integer city=ObjectUtil.parseInt(String.valueOf(model.get("city")));
			if(city!=null&&city>0){
				param.setCity(city);
			}
			Integer area=ObjectUtil.parseInt(String.valueOf(model.get("area")));
			if(area!=null&&area>0){
				param.setArea(area);
			}
			String streetdetail=String.valueOf(model.get("streetdetail"));
			if(!ObjectUtil.isEmpty(streetdetail)&&!"null".equals(streetdetail)){
				param.setStreetdetail(streetdetail);
			}
			return param;
		}
		return null;
		
	}
}
