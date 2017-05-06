package com.bbyiya.pic.web.ibs;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ibs/customer")
public class CustomerController extends SSOController{
	@Resource(name = "pic_memberMgtService")
	private IPic_MemberMgtService memberMgtService;
	
	/**
	 * �ҵĿͻ��б�
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/list")
	public String memberslist() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=memberMgtService.findCustomerslistByAgentUserId(user.getUserId());
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * IBS���ݿͻ�UserId�õ��ͻ��Ĺ����¼
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/customerBuylist")
	public String customerBuylist(Long userId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=memberMgtService.findCustomersBuylistByUserId(userId);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * �������޸Ŀͻ���Ϣ
	 * @param customerJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrEdit")
	public String addOrEdit(String customerJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			UAgentcustomers param=Json2Objects.getParam_UAgentcustomers(customerJson);
			if(param!=null&&param.getCustomerid()!=null&&param.getCustomerid()>0){
				rq=memberMgtService.editCustomer(user.getUserId(), param); 
			}else {
				rq=memberMgtService.addCustomer(user.getUserId(), param); 
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ɾ���ҵĿͻ�
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dele")
	public String dele(long customerId) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=memberMgtService.deleteCustomer(user.getUserId(), customerId); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
