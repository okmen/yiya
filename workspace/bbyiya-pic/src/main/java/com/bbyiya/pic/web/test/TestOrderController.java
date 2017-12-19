package com.bbyiya.pic.web.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OOrderproductphotosMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproductphotos;
import com.bbyiya.service.IBasePayService;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ImgDomainUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.upload.FileUploadUtils_qiniu;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;
import com.qiniu.storage.model.FetchRet;

@Controller
@RequestMapping(value = "/test2")
public class TestOrderController  extends SSOController{
	@Resource(name = "basePayServiceImpl")
	private IBasePayService orderMgtService;
	
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	
	@ResponseBody 
	@RequestMapping(value = "/urlparam")
	public String urlparam(String weburl) throws Exception {
		ReturnModel rq = new ReturnModel();
		Map<String, String> map= ObjectUtil.getUrlParam(weburl);
		if(map!=null){
			rq.setBasemodle(map); 
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody 
	@RequestMapping(value = "/ss")
	public String templateMessageSend(String value1,String value2) throws Exception {
		ReturnModel rq = new ReturnModel();
		Double a1=ObjectUtil.parseDouble(value1);
		Double a2=ObjectUtil.parseDouble(value2);
		double B1=a2.doubleValue()-a1.doubleValue();
		double b2=ObjectUtil.doubleSub(a2, a1);
		rq.setBasemodle("b1:"+B1+"。b2:"+b2);
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody 
	@RequestMapping(value = "/accountlogs")
	public String templateMessageSend(long  userId,int type, int index,int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq=accountService.findAcountsLogsPageResult(userId,null, type, index, size);
		
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody 
	@RequestMapping(value = "/addAccountlogs")
	public String addAccountlog(long  userId,int type, double amount,String payId,String transNo) throws Exception {
		ReturnModel rq = new ReturnModel();
		if(accountService.add_accountsLog(userId, type, amount, payId, transNo)){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("success");
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 支付成功测试用
	 * @param payId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody 
	@RequestMapping(value = "/orderpaytest")
	public String orderpaytest(String payId) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		String currentDomain=ConfigUtil.getSingleValue("currentDomain");
		if(!ObjectUtil.isEmpty(currentDomain)&&currentDomain.contains("photo-net.")){
			if(orderMgtService.paySuccessProcess(payId)){
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("pay success!"); 
			}
		}else {
			rq.setStatusreson("false!");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody 
	@RequestMapping(value = "/orderAmountDistribute")
	public String distributeOrderAmount(String orderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		String currentDomain=ConfigUtil.getSingleValue("currentDomain");
		if(!ObjectUtil.isEmpty(currentDomain)&&currentDomain.contains("photo-net.")){
			orderMgtService.distributeOrderAmount(orderId);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("ok!");
		}else { 
			rq.setStatusreson("false!");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@Autowired
	private OOrderproductphotosMapper ophotoMapper;
	
	@ResponseBody 
	@RequestMapping(value = "/orderImgs")
	public String orderImgs(String orderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		List<OOrderproductphotos> list=	ophotoMapper.findImgNotGood();
		if(list!=null&&list.size()>0){
			for (OOrderproductphotos oo : list) {
				FetchRet result= FileUploadUtils_qiniu.fetch(oo.getImgurl());
				if(result!=null){
					String imgnew=ImgDomainUtil.getImageUrl_Full(result.key) ;
					oo.setImgurl(imgnew);
					ophotoMapper.updateByPrimaryKeySelective(oo);
				}
			}
			rq.setBasemodle(list.size());
		}
//		List<OOrderproductphotos> photos= ophotoMapper.findOrderProductPhotosByProductOrderId(orderId);
//		if(photos!=null&&photos.size()>0){
//			List<Map<String, String>> maplistList=new ArrayList<Map<String,String>>();
//			for (OOrderproductphotos oo : photos) {
//				FetchRet result= FileUploadUtils_qiniu.fetch(oo.getImgurl());
//				if(result!=null){
//					Map<String, String> map=new HashMap<String, String>();
//					String imgnew=ImgDomainUtil.getImageUrl_Full(result.key) ;
//					map.put("imgNew", imgnew);
//					map.put("imgOld", oo.getImgurl());
//					maplistList.add(map);
//					oo.setImgurl(imgnew);
//					ophotoMapper.updateByPrimaryKeySelective(oo);
//				}
//				rq.setBasemodle(maplistList); 
//			}
//		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
}
