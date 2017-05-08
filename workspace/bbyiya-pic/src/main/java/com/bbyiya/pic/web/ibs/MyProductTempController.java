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
	public String addMyProductTemp(String title,String remark,String productid) throws Exception {
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
			rq=producttempService.addMyProductTemp(user.getUserId(), title, remark,ObjectUtil.parseLong(productid));
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
	 * 保存模板的二维码图片
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/downProductTempRQcode")
	public String downProductTempRQcode(String url) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=producttempService.saveProductTempRQcode(url);
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
		//String versionString=DateUtil.getTimeStr(new Date(), "yyyyMMddHHMMss"); 
		String redirct_url="currentPage?workId="+cartId;	
		String urlstr="https://mpic.bbyiya.com/login/transfer?m=1&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
		//String url="https://mpic.bbyiya.com/common/generateQRcode?urlstr=https://mpic.bbyiya.com/login/transfer?m=1&redirct_url="+URLEncoder.encode(redirct_url,"utf-8");
		String url="https://mpic.bbyiya.com/common/generateQRcode?urlstr="+URLEncoder.encode(urlstr,"utf-8");
		
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
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
