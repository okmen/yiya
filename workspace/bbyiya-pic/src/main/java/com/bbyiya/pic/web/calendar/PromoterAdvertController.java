package com.bbyiya.pic.web.calendar;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.TiPromoteradvertcoustomerMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.TiPromoteradvertcoustomer;
import com.bbyiya.model.TiPromoteradvertinfo;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_PromoterAdvertService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
/**
 * 分享广告接口
 * @author Max
 *
 */
@Controller
@RequestMapping(value = "/ti_promoterAdvert")
public class PromoterAdvertController extends SSOController {
	
	@Autowired
	private TiPromoteradvertcoustomerMapper advertCustomerMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	//分享广告service
	@Resource(name = "ti_PromoterAdvertService")
	private ITi_PromoterAdvertService advertService;
	
	/**
	 * 获取广告banner信息，新增浏览次数
	 * @param advertid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/advertBannerInfo")
	public String advertBannerInfo(int advertid)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null){
			//分享广告详情
			TiPromoteradvertinfo advertInfo=advertService.addViewCountReurnTiPromoteradvertinfo(user, advertid); //advertService.getTiPromoteradvertinfo(advertid);
			if(advertInfo!=null){
				rq.setBasemodle(advertInfo);
			}
			rq.setStatu(ReturnStatus.Success);
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 广告详情页--信息详细
	 * @param advertid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/advertinfo")
	public String advertinfo(int advertid)throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user=super.getLoginUser();
		if(user!=null){
			//分享广告详情
			TiPromoteradvertinfo advertInfo=advertService.addClickCountReurnTiPromoteradvertinfo(user, advertid); //advertService.getTiPromoteradvertinfo(advertid);
			if(advertInfo!=null){
				rq.setBasemodle(advertInfo);
			}
			rq.setStatu(ReturnStatus.Success);
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 分享广告活动 -报名
	 * @param name
	 * @param phone
	 * @param advertId
	 * @param province
	 * @param city
	 * @param district
	 * @param streetDetail
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addAdvertcustomer")
	public String addAdvertcustomer(String name,String phone,int advertId, int province,int city,int district,String streetDetail)throws Exception {
		ReturnModel rq=new ReturnModel();
		if(advertId<=0){
			rq.setStatusreson("参数有误");
			return JsonUtil.objectToJsonStr(rq);
		}
		if(ObjectUtil.isEmpty(name)){
			rq.setStatusreson("姓名不能为空！");
			return JsonUtil.objectToJsonStr(rq);
		}
		if(!ObjectUtil.isMobile(phone)){
			rq.setStatusreson("手机号不正确！");
			return JsonUtil.objectToJsonStr(rq);
		}
		TiPromoteradvertcoustomer customer=new TiPromoteradvertcoustomer();
		customer.setAdvertid(advertId);
		customer.setName(name);
		customer.setMobilephone(phone);
		customer.setProvince(province);
		customer.setCity(city);
		customer.setDistrict(district);
		customer.setStreetdetail(streetDetail);
		customer.setCreatetime(new Date()); 
		customer.setAddress(regionService.getProvinceName(province)+regionService.getCityName(city)+regionService.getAresName(district)+streetDetail);
		advertCustomerMapper.insert(customer); 
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
}
