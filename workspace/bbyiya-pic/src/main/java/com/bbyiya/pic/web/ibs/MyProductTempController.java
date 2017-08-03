package com.bbyiya.pic.web.ibs;

import java.net.URLEncoder;
import java.util.Date;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
import com.bbyiya.pic.vo.order.SearchOrderParam;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/temp")
public class MyProductTempController extends SSOController {
	
	@Resource(name = "ibs_MyProductTempService")
	private IIbs_MyProductTempService producttempService;
	
	
	
	/**
	 * 添加模板
	 * @param title 模板标题
	 * @param remark 备注
	 * @param productid 产品款示
	 * @param needverifer 是否需要审核
	 * @param discription 活动需知
	 * @param codeurl  二维码图片
	 * @returnp
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addMyProductTemp")
	public String addMyProductTemp(String myproductTempJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			MyProductTempAddParam param = (MyProductTempAddParam)JsonUtil.jsonStrToObject(myproductTempJson,MyProductTempAddParam.class);			
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
				rq.setStatusreson("产品ID不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getStyleId())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动奖品不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getNeedverifer()==1&&ObjectUtil.isEmpty(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动需知不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDiscription())&&!ObjectUtil.validSqlStr(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动需知存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getCodeurl())&&!ObjectUtil.isEmpty(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("设置了二维码简介，二维码图片不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getCodesm())&&!ObjectUtil.validSqlStr(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("二维码简介在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.addMyProductTemp(user.getUserId(), param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 
	 * @param title
	 * @param remark
	 * @param tempid
	 * @param needverifer
	 * @param discription
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editTempCodeUrl")
	public String editTempCodeUrl(Integer tempid,String codeurl,String codesm,String discription,String logourl) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(tempid)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数错误，tempid为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(codesm)&&!ObjectUtil.validSqlStr(codesm)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("二维码简介说明在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(codeurl)&&!ObjectUtil.isEmpty(codesm)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("设置了二维码简介说明，二维码图片不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.editTempCodeUrl(tempid,codeurl,codesm,discription,logourl);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 修改模板
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editMyProductTemp")
	public String editMyProductTemp(String myproductTempJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			MyProductTempAddParam param = (MyProductTempAddParam)JsonUtil.jsonStrToObject(myproductTempJson,MyProductTempAddParam.class);			
			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTempid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动ID不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动名称存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getNeedverifer()==1&&ObjectUtil.isEmpty(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动需知不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDiscription())&&!ObjectUtil.validSqlStr(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动需知存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getCodeurl())&&!ObjectUtil.isEmpty(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("设置了二维码简介，二维码图片不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getCodesm())&&!ObjectUtil.validSqlStr(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("二维码简介说明存在危险字符!");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			rq=producttempService.editMyProductTemp(param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 启用或禁用模板
	 * @param type 1:启用   0禁用  3 结束活动  
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editMyProductTempStatus")
	public String editMyProductStatus(int type,int tempid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.editMyProductTempStatus(type, tempid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 删除模板
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteMyProductTemp")
	public String deleteMyProductTemp(int tempid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.deleteMyProductTemp(tempid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取影楼模板列表
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyProductTempList")
	public String getMyProductTempList(int index,int size,Integer status,String keywords,Integer type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.findMyProductTempList(index, size, user.getUserId(),status,keywords,type);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取影楼模板列表
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyProductTempById")
	public String getMyProductTempById(Integer tempid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.getMyProductTempById(tempid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 获取影楼模板待审核用户
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getMyProductTempApplyCheckList")
	public String getMyProductTempApplyCheckList(int tempid,Integer index,Integer size) throws Exception {
		ReturnModel rq=new ReturnModel();
		if(index==null) index=0;
		if(size==null) size=0;
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.getMyProductTempApplyCheckList(index, size, user.getUserId(), tempid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 审核通过或拒绝影楼模板申请用户
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_TempApplyUser")
	public String audit_TempApplyUser(Long tempApplyId,Integer status) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(status==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("status参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			//判断用户是否有权限操作
			if(!(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.salesman))){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("没有权限做此操作！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.audit_TempApplyUser(user.getUserId(), tempApplyId, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 影楼员工负责模板信息列表
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/find_UBranchUserOfTempList")
	public String find_UBranchUserOfTempList(Integer index,Integer size,Integer tempid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.find_BranchUserOfTemp(index, size, user.getUserId(), tempid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 设置员工模板负责二维码推广权限
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setUserTempPermission")
	public String setUserTempPermission(Long userId,Integer tempid,Integer status) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(status==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("status参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setUserTempPermission(userId, tempid, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 设置员模板审核负责权限
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setUserTempVerfiyPermission")
	public String setUserTempVerfiyPermission(Long userId,Integer tempid,Integer status) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(status==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("status参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setUserTempVerfiyPermission(userId, tempid, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 审核模板申请用户的作品不通过
	 * @param tempApplyId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/audit_TempApplyProduct")
	public String audit_TempApplyProduct(Long cartid,Integer status,String reason) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(status==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("status参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.audit_TempApplyProduct(user.getUserId(), cartid, status,reason);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 设置活动报名人数限制
	 * @param tempid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setTempMaxApplyCount")
	public String setTempMaxApplyCount(Integer tempid,Integer maxApplyCount) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(tempid==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("tempid参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setTempMaxApplyCount(user.getUserId(),tempid,maxApplyCount);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 设置活动完成条件
	 * @param tempid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setTempCompletecondition")
	public String setTempCompletecondition(Integer tempid,Integer blessCount,Integer maxCompleteCount,String amountlimit) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(tempid==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("tempid参数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setTempCompletecondition(user.getUserId(),tempid,blessCount,maxCompleteCount,ObjectUtil.parseDouble(amountlimit));
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
	@RequestMapping(value = "/downProductTempRQcode")
	public String downProductTempRQcode(String cartId,String companyUserid,String type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String redirct_url="apply/form?workId="+URLEncoder.encode(cartId,"utf-8")+"&type="+URLEncoder.encode(type,"utf-8");
			if(ObjectUtil.parseLong(companyUserid)>0){
				redirct_url=redirct_url+"&sid="+URLEncoder.encode(companyUserid.toString(),"utf-8");
			}
			String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(user.getUserId().toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");				
			rq=producttempService.saveProductTempRQcode(urlstr);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 生成模板的二维码图片
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getProductTempRQcode")
	public String getProductTempRQcode(String cartId,String type) throws Exception {
		ReturnModel rq=new ReturnModel();		
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String redirct_url="apply/form?workId="+cartId+"&type="+type;	
			String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"uid="+URLEncoder.encode(user.getUserId().toString(),"utf-8")+"&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
			String url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");
			
			rq.setBasemodle(url);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("生成模板二维码成功");
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
