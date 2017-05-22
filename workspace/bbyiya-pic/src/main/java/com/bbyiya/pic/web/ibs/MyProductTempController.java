package com.bbyiya.pic.web.ibs;

import java.net.URLEncoder;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
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
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addMyProductTemp")
	public String addMyProductTemp(String title,String remark,String productid,String needverifer,String discription) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(title)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("模板名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(productid)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("产品ID不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.parseInt(needverifer)==1&&ObjectUtil.isEmpty(discription)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动需知不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.addMyProductTemp(user.getUserId(), title, remark,ObjectUtil.parseLong(productid),ObjectUtil.parseInt(needverifer),discription );
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
	public String editMyProductTemp(String title,String remark,Integer tempid,String needverifer,String discription) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(title)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("模板名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.parseInt(needverifer)==1&&ObjectUtil.isEmpty(discription)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("活动需知不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.editMyProductTemp(title, remark, tempid,ObjectUtil.parseInt(needverifer),discription );
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 启用或禁用模板
	 * @param type
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
	public String getMyProductTempList(int index,int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.findMyProductTempList(index, size, user.getUserId());
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
			rq=producttempService.audit_TempApplyUser(user.getUserId(), tempApplyId, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 保存模板的二维码图片
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/downProductTempRQcode")
	public String downProductTempRQcode(String cartId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String redirct_url="currentPage?workId="+URLEncoder.encode(cartId,"utf-8")+"&uid="+URLEncoder.encode(user.getUserId().toString(),"utf-8");	
			String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"redirct_url="+URLEncoder.encode(redirct_url,"utf-8");				
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
	public String getProductTempRQcode(String cartId) throws Exception {
		ReturnModel rq=new ReturnModel();		
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			String redirct_url="currentPage?workId="+cartId+"&uid="+user.getUserId();	
			String urlstr= ConfigUtil.getSingleValue("shareulr-base")+"redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
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
