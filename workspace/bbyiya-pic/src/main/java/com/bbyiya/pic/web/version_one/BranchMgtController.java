package com.bbyiya.pic.web.version_one;

import java.util.Date;







import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.UBranchinfotempMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserStatusEnum;
import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchinfotemp;
import com.bbyiya.pic.service.IPic_BranchMgtService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/ibs/branch")
public class BranchMgtController extends SSOController {
	@Resource(name = "pic_BranchMgtService")
	private IPic_BranchMgtService branchService;
	
	@Autowired
	private UBranchinfotempMapper tempMapper;
	@Autowired
	private EErrorsMapper logger;
	/**
	 * B01 ���̱���
	 * @param companyJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/companySubmit")
	public String companySubmit( String companyJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		try {
			
			UBranchinfotemp model= Json2Objects.getParam_UBranchinfotemp(companyJson);// (UBranchinfotemp)JsonUtil.jsonStrToObject(companyJson, UBranchinfotemp.class);
			if(model!=null){
				if(ObjectUtil.isEmpty(model.getPhone())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("�ֻ��Ų���Ϊ��");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(model.getContactname())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("��ϵ�˲���Ϊ��");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(model.getCompanyname())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("��˾��Ϣ����Ϊ��");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(!(ObjectUtil.validSqlStr(model.getPhone())&&ObjectUtil.validSqlStr(model.getCompanyname())&&ObjectUtil.validSqlStr(model.getContactname()))){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("���ڷǷ��ַ�");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("��������");
				return JsonUtil.objectToJsonStr(rq);
			}
			model.setCreatetime(new Date()); 
			tempMapper.insertSelective(model);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("�ύ�ɹ���");
		} catch (Exception e) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��������001");
			return JsonUtil.objectToJsonStr(rq);
		} 
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ���ݵ�����ȡ������
	 * @param province
	 * @param city
	 * @param district
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getBranchAreaPrice")
	public String getBranchAreaPrice(String province,String city,String district) throws Exception {
		ReturnModel rq = branchService.getBranchAreaPrice(ObjectUtil.parseInt(province) , ObjectUtil.parseInt(city), ObjectUtil.parseInt(district));
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 *  ����������
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/agentApply")
	public String agentApply(String agentJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {
					UAgentapply applyInfo=(UAgentapply)JsonUtil.jsonStrToObject(agentJson, UAgentapply.class);
					rq =branchService.applyAgent(user.getUserId(), applyInfo);
				} catch (Exception e) {
					// TODO: handle exception
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("��������");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("δ���ע��");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 *  �ֵ�����
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/branchApply")
	public String branchApply(String branchJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(user.getStatus()!=null&&user.getStatus().intValue()==Integer.parseInt(UserStatusEnum.ok.toString())){
				try {
					UBranches applyInfo=(UBranches)JsonUtil.jsonStrToObject(branchJson, UBranches.class);
					rq =branchService.applyBranch(user.getUserId(), applyInfo);
				} catch (Exception e) {
					// TODO: handle exception
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("��������");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.LoginError_2);
				rq.setStatusreson("δ���ע��");
				return JsonUtil.objectToJsonStr(rq);
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ��ȡ����Ԫ�����������жϴ���Ԫ��
	 * @param areaCode
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getAgentAreas")
	public String getAgentAreas(Integer areaCode) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.getAgentArea(areaCode);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
