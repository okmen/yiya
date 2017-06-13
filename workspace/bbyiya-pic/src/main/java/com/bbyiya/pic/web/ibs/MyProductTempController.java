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
	 * ���ģ��
	 * @param title ģ�����
	 * @param remark ��ע
	 * @param productid ��Ʒ��ʾ
	 * @param needverifer �Ƿ���Ҫ���
	 * @param discription ���֪
	 * @param codeurl  ��ά��ͼƬ
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
				rq.setStatusreson("������ȫ");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("����Ʋ���Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getProductid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ƷID����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getStyleId())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���Ʒ����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("����ƴ���Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getNeedverifer()==1&&ObjectUtil.isEmpty(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���֪����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDiscription())&&!ObjectUtil.validSqlStr(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���֪����Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getCodesm())&&!ObjectUtil.validSqlStr(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ά������˵����Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.addMyProductTemp(user.getUserId(), param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
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
	public String editTempCodeUrl(Integer tempid,String codeurl,String codesm) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(tempid)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��������tempidΪ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(codesm)&&!ObjectUtil.validSqlStr(codesm)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ά������˵����Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.editTempCodeUrl(tempid,codeurl,codesm);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * �޸�ģ��
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
				rq.setStatusreson("������ȫ");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTempid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("�ID����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("����Ʋ���Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			if(!ObjectUtil.isEmpty(param.getTitle())&&!ObjectUtil.validSqlStr(param.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("����ƴ���Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getNeedverifer()==1&&ObjectUtil.isEmpty(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���֪����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getDiscription())&&!ObjectUtil.validSqlStr(param.getDiscription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���֪����Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getCodesm())&&!ObjectUtil.validSqlStr(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ά������˵������Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			rq=producttempService.editMyProductTemp(param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * ���û����ģ��
	 * @param type 1:����   0����  3 �����  
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
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ɾ��ģ��
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
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ��ȡӰ¥ģ���б�
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
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ��ȡӰ¥ģ�������û�
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
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ���ͨ����ܾ�Ӱ¥ģ�������û�
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
				rq.setStatusreson("status��������Ϊ�գ�");
				return JsonUtil.objectToJsonStr(rq);
			}
			//�ж��û��Ƿ���Ȩ�޲���
			if(!(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)||ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.salesman))){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("û��Ȩ�����˲�����");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.audit_TempApplyUser(user.getUserId(), tempApplyId, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * Ӱ¥Ա������ģ����Ϣ�б�
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
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * ����Ա��ģ�帺���ά���ƹ�Ȩ��
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
				rq.setStatusreson("status��������Ϊ�գ�");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setUserTempPermission(userId, tempid, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * ����Աģ����˸���Ȩ��
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
				rq.setStatusreson("status��������Ϊ�գ�");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setUserTempVerfiyPermission(userId, tempid, status);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * ���ģ�������û�����Ʒ��ͨ��
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
				rq.setStatusreson("status��������Ϊ�գ�");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.audit_TempApplyProduct(user.getUserId(), cartid, status,reason);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ���û������������
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
				rq.setStatusreson("tempid��������Ϊ�գ�");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setTempMaxApplyCount(user.getUserId(),tempid,maxApplyCount);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ���û�������
	 * @param tempid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/setTempCompletecondition")
	public String setTempCompletecondition(Integer tempid,Integer blessCount,Integer maxCompleteCount) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(tempid==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("tempid��������Ϊ�գ�");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=producttempService.setTempCompletecondition(user.getUserId(),tempid,blessCount,maxCompleteCount);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ����ģ��Ķ�ά��ͼƬ
	 * @param cartId
	 * @param companyUserid Ա��ID
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
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ����ģ��Ķ�ά��ͼƬ
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
			rq.setStatusreson("����ģ���ά��ɹ�");
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
		
}
