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
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.pic.vo.agent.UBranchVo;
import com.bbyiya.service.IRegionService;
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
	@Resource(name = "pic_productService")
	private IPic_ProductService proService;
	@Autowired
	private UBranchinfotempMapper tempMapper;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
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
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("��������101");
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
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("��������101");
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
	/**
	 * �ж��û�����������״̬
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getApplyStatus")
	public String getApplyStatus(Integer type) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(type!=null&&type==1){
				rq=branchService.getAgentApplyStatusModel(user.getUserId());
			}else {
				rq=branchService.getBranchApplyStatusModel(user.getUserId());
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * ��½��õ���������Ϣ
	 * @param type
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getBranchInfo")
	public String getBranchInfo() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			UBranchVo branch=branchService.getBranchInfo(user.getUserId());	
			rq.setBasemodle(branch);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("��ȡ��������Ϣ�ɹ���");
			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �޸Ĵ������ջ���ַ
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editBranchAddress")
	public String editBranchAddress(String streetdetail) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.editBranchAddress(user.getUserId(), streetdetail);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("�޸Ĵ�������Ϣ�ɹ���");
			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * �����������������
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addUserResponses")
	public String addUserResponses(String content) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.addUserResponses(user.getUserId(), content);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * IBS��ȡϵͳ��Ϣ֪ͨ�б�
	 * @param content
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getSysMessageList")
	public String getSysMessageList(int index,int size,String startTimeStr,String endTimeStr) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=branchService.getSysMessageList(index, size, startTimeStr, endTimeStr);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
			return JsonUtil.objectToJsonStr(rq);
		}
		
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
