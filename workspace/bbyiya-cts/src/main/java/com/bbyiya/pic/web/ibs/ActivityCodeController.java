package com.bbyiya.pic.web.ibs;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.ibs.IIbs_ActivityCodeService;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/temp")
public class ActivityCodeController extends SSOController {
	
	@Resource(name = "ibs_ActivityCodeService")
	private IIbs_ActivityCodeService activitycodeService;
	
	
	
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
	@RequestMapping(value = "/addActivityCode")
	public String addActivityCode(String myproductTempJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			MyProductTempAddParam param = (MyProductTempAddParam)JsonUtil.jsonStrToObject(myproductTempJson,MyProductTempAddParam.class);			
			if (param == null) {
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("������ȫ");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getProductid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��Ӧ��Ʒ����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getStyleId())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("���Ʒ����Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(param.getApplycount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("������������Ϊ��!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(param.getApplycount()!=null&&param.getApplycount()<=0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("�����������������!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(!ObjectUtil.isEmpty(param.getCodesm())&&!ObjectUtil.validSqlStr(param.getCodesm())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��ά������˵����Σ���ַ�!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			rq=activitycodeService.addActivityCode(user.getUserId(), param);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �õ�������εĵĻ����Ʒ�б�
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findMyProductslistForActivityCode")
	public String findMyProductslistForActivityCode(Integer tempid,Integer activeStatus,
			@RequestParam(required = false, defaultValue = "")String keywords,
			@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=activitycodeService.findMyProductslistForActivityCode(user.getUserId(), tempid,activeStatus,keywords, index, size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ɾ�����
	 * @param tempId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteActivityCode")
	public String deleteActivityCode(String codeno) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=activitycodeService.deleteActivityCode(codeno);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
		
}
