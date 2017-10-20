package com.bbyiya.pic.web.calendar;

import java.net.URLEncoder;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.AddressTypeEnum;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.pic.service.calendar.IIbs_CalendarActivityService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.pic.vo.calendar.CalendarActivityAddParam;
import com.bbyiya.pic.vo.calendar.WorkForCustomerParam;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.address.OrderaddressVo;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/calendar/act/")
public class CalendarActivityController extends SSOController {
	
	@Resource(name = "ibs_CalendarActivityService")
	private IIbs_CalendarActivityService calendarActivityService;
	
	
	
	/**
	 * 创建日历活动
	 * @param myproductTempJson
	 * @param productstyleJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addCalendarActivity")
	public String addCalendarActivity(String activityJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			CalendarActivityAddParam param = (CalendarActivityAddParam)JsonUtil.jsonStrToObject(activityJson,CalendarActivityAddParam.class);
			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getProductid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请选择活动奖品!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getFreecount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getExtCount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("完成条件的分享数不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getFreecount()!=null&&param.getFreecount()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能小于0!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getExtCount()!=null&&param.getExtCount()<5){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("完成条件的分享数必须大于等于5个!");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDescription())&&!ObjectUtil.validSqlStr(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
					
			rq=calendarActivityService.addCalendarActivity(user.getUserId(),param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/editCalendarActivity")
	public String editCalendarActivity(String activityJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			CalendarActivityAddParam param = (CalendarActivityAddParam)JsonUtil.jsonStrToObject(activityJson,CalendarActivityAddParam.class);
			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getFreecount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getFreecount()!=null&&param.getFreecount()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("邀请总数不能小于零!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDescription())&&!ObjectUtil.validSqlStr(param.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动详情存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			rq=calendarActivityService.editCalendarActivity(param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 合成图片调的接口
	 * @param actid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/composeActImg")
	public String composeActImg(Integer actid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if (actid==null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("actid不能为空");
				return JsonUtil.objectToJsonStr(rq);
			}	
			rq=calendarActivityService.composeActImg(user.getUserId(), actid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/savecomposeActImg")
	public String savecomposeActImg(Integer actid,String actimg) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if (actid==null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("actid不能为空");
				return JsonUtil.objectToJsonStr(rq);
			}	
			rq=calendarActivityService.savecomposeActImg(actid,actimg);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * ibs推广者得到活动列表
	 * @param index
	 * @param size
	 * @param status
	 * @param keywords
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getCalendarActivityList")
	public String getCalendarActivityList(int index,int size,Integer status,String keywords,Integer type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=calendarActivityService.findCalendarActivityList(index, size, user.getUserId(),status,keywords,type);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 得到活动制作进度情况列表
	 * @param index
	 * @param size
	 * @param status
	 * @param keywords
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getActWorkListByActId")
	public String getActWorkListByActId(int index,int size,Integer actid,Integer status,String keywords) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=calendarActivityService.getActWorkListByActId(index, size,actid,status,keywords);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 编辑活动备注信息
	 * @param tempid
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editActRemark")
	public String editActRemark(Integer actid,String remark) throws Exception {
		ReturnModel rq=new ReturnModel();
		rq=calendarActivityService.editActivityRemark(actid, remark);
		return JsonUtil.objectToJsonStr(rq);
		
	}
	
	
	/**
	 * 员工活动权限列表
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findActivityoffList")
	public String findActivityoffList(Integer index,Integer size,Integer actid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=calendarActivityService.findActivityoffList(index, size, user.getUserId(), actid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 设置员工负责活动推广权限
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setUserActPermission")
	public String setUserActPromotePermission(Long userId,Integer actid,Integer status) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(status==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("status参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=calendarActivityService.setUserActPromotePermission(userId, actid, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 新增代客制作
	 * @param myproductTempJson
	 * @param productstyleJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addWorkForCustomer")
	public String addWorkForCustomer(String activityJson,String addressJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			WorkForCustomerParam workparam = (WorkForCustomerParam)JsonUtil.jsonStrToObject(activityJson,WorkForCustomerParam.class);
			OrderaddressVo addressparam=(OrderaddressVo)JsonUtil.jsonStrToObject(addressJson,OrderaddressVo.class);
			
			if (workparam == null||addressparam==null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(workparam.getProductId())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请选择产品!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(workparam.getStyleId())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请选择款式!");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			if(workparam.getDetails()==null||workparam.getDetails().size()<=0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请上传图片!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if((workparam.getNeedShareCount()==null||workparam.getNeedShareCount().intValue()==0)&&(workparam.getNeedRedpacketTotal()==null||workparam.getNeedRedpacketTotal().doubleValue()<=0)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请选择完成条件!");
				return JsonUtil.objectToJsonStr(rq);
			}
			//如果是选择是客户地址
			if(addressparam.getAddressType()==Integer.parseInt(AddressTypeEnum.cusaddr.toString())){
				if(ObjectUtil.isEmpty(addressparam.getProvince())||ObjectUtil.isEmpty(addressparam.getCity())||ObjectUtil.isEmpty(addressparam.getDistrict())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("请选择省市区!");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(addressparam.getReciver())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("请填写客户名称!");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(addressparam.getPhone())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("请填写客户联系方式!");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(addressparam.getStreetdetail())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("请填写详细地址!");
					return JsonUtil.objectToJsonStr(rq);
				}
			}	
			rq=calendarActivityService.addWorkForCustomer(user.getUserId(),workparam,addressparam);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 代客制作列表
	 * @param myproductTempJson
	 * @param productstyleJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/workForCustomerList")
	public String workForCustomerList(@RequestParam(required = false, defaultValue = "1") Integer index,@RequestParam(required = false, defaultValue = "10") Integer size,@RequestParam(required = false, defaultValue = "1") String keywords) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(index==null)index=1;
			if(size==null)size=10;
			rq=calendarActivityService.workForCustomerList(user.getUserId(),index,size,keywords);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 保存模板的二维码图片
	 * @param cartId
	 * @param companyUserid 员工ID
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/downWorkForCusRQcode")
	public String downWorkForCusRQcode(String workId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String redirct_url="assistant?workId="+workId;	
			String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(user.getUserId().toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
			rq=calendarActivityService.saveRQcode(urlstr);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
}
