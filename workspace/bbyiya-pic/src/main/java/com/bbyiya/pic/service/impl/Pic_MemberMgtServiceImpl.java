package com.bbyiya.pic.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UAgentcustomersMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.UAgentcustomers;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_MemberMgtService;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;

@Service("pic_memberMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_MemberMgtServiceImpl implements IPic_MemberMgtService{
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	
	@Autowired
	private UBranchusersMapper branchusersMapper;
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UBranchesMapper branchesMapper;
	@Autowired
	private UAgentcustomersMapper customerMapper;
	
	public ReturnModel findBranchUserslistByBranchUserId(Long branchUserId){
		ReturnModel rqModel=new ReturnModel();
		List<UBranchusers> list= branchusersMapper.findMemberslistByBranchUserId(branchUserId);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setBasemodle(list);
		return rqModel;
	}
	
	public ReturnModel addBranchUser(Long branchUserId,UBranchusers param){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		if(param==null){
			rqModel.setStatusreson("��������");
			return rqModel;
		}
		if(ObjectUtil.isEmpty(param.getPhone())){ 
			rqModel.setStatusreson("�ֻ��Ų���Ϊ��");
			return rqModel;
		}
		//���Ӱ¥���
		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null||branch.getStatus()==null||branch.getStatus().intValue()!=Integer.parseInt(BranchStatusEnum.ok.toString())){
			rqModel.setStatusreson("������Ӱ¥����Ա����ʱû��Ȩ�ޣ�");
			return rqModel;
		}
		//��ⱻ��ӵ��û���ݣ�������ֻ���
		UUsers member= usersMapper.getUUsersByPhone(param.getPhone());
		if(member!=null){
			//�鿴�û��Ƿ��Ѿ��� ������Ա
			UBranchusers usBranchusers= branchusersMapper.selectByPrimaryKey(member.getUserid()); 
			if(usBranchusers!=null){
				//������Ǳ�Ӱ¥�ķ�����Ա����Ȼ���Ա����
				if(usBranchusers.getBranchuserid()!=null&&usBranchusers.getBranchuserid().intValue()==branchUserId){
					rqModel.setStatu(ReturnStatus.ParamError);
					rqModel.setStatusreson("���û��Ѿ�����Ա���������ظ���ӣ�");
					return rqModel;
				}
			}
			param.setBranchuserid(branchUserId);
			param.setAgentuserid(branch.getAgentuserid());
			param.setUserid(member.getUserid());
			param.setStatus(1);
			param.setCreatetime(new Date());
			branchusersMapper.insert(param);
			
			//����ӵ��û�����ݱ�ʾ
			userBasic.addUserIdentity(member.getUserid(),UserIdentityEnums.salesman); 
			
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setStatusreson("��ӳɹ���");
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("�û������ڣ����߸��ֻ�δ�󶨣���");
		}
		return rqModel;
	}
	
	public ReturnModel delBranchUser(Long branchUserId,Long userId){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		//���Ӱ¥���
		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null||branch.getStatus()==null||branch.getStatus().intValue()!=Integer.parseInt(BranchStatusEnum.ok.toString())){
			rqModel.setStatusreson("������Ӱ¥����Ա����ʱû��Ȩ�ޣ�");
			return rqModel;
		}
		UBranchusers usBranchusers= branchusersMapper.selectByPrimaryKey(userId); 
		if(usBranchusers!=null){
			if(usBranchusers.getBranchuserid()!=null&&usBranchusers.getBranchuserid().longValue()==branchUserId){
				branchusersMapper.deleteByPrimaryKey(userId);
				//�Ƴ��û���ʶ
				userBasic.removeUserIdentity(userId,UserIdentityEnums.salesman); 
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setStatusreson("ɾ���ɹ���"); 
			}
		}
		return rqModel;
	}
	
	public ReturnModel findCustomersByBranchUserId(Long branchUserId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		List<UAgentcustomers> customers= customerMapper.findCustomersByBranchUserId(branchUserId);
		rq.setBasemodle(customers);
		return rq;
	}
	
	public ReturnModel editCustomer(Long branchUserId,UAgentcustomers param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(param==null||param.getCustomerid()==null||param.getCustomerid()<=0){
			rq.setStatusreson("��������");
			return rq;
		}
		UAgentcustomers model= customerMapper.selectByPrimaryKey(param.getCustomerid());
		if(model!=null){
			if(!ObjectUtil.isEmpty(param.getName())){
				model.setName(param.getName());
			}
			if(param.getStatus()!=null){
				model.setStatus(param.getStatus());
			}
			if(!ObjectUtil.isEmpty(param.getRemark())){
				model.setRemark(param.getRemark());
			}
			if(!ObjectUtil.isEmpty(param.getPhone())){
				model.setPhone(param.getPhone());
				UUsers customerUsers=usersMapper.getUUsersByPhone(param.getPhone());
				if(customerUsers!=null){
					model.setUserid(customerUsers.getUserid()); 
				}
			}
			customerMapper.updateByPrimaryKeySelective(model);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("�޸ĳɹ�");
		}else {
			rq.setStatusreson("�Ҳ�����Ӧ�Ŀͻ���"); 
		} 
		return rq;
	}
	
	public ReturnModel deleteCustomer(Long branchUserId,Long customerId){
		ReturnModel rq=new ReturnModel();
		UAgentcustomers agentcustomers= customerMapper.selectByPrimaryKey(customerId);
		if(agentcustomers!=null){
			if(agentcustomers.getBranchuserid()!=null&&agentcustomers.getBranchuserid().longValue()==branchUserId){
				customerMapper.deleteByPrimaryKey(customerId);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("ɾ���ɹ�");
			}else {
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("������Ŀͻ����޷�ɾ����");
				return rq;
			}
		}
		rq.setStatu(ReturnStatus.SystemError);
		rq.setStatusreson("�Ҳ�����Ӧ�Ŀͻ���");
		return rq; 
	}
	
	public ReturnModel addCustomer(Long branchUserId,UAgentcustomers param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(param==null|| ObjectUtil.isEmpty(param.getPhone())|| !ObjectUtil.isMobile(param.getPhone()) ){
			rq.setStatusreson("��������");
			return rq;
		}
		UUsers branchUsers=usersMapper.selectByPrimaryKey(branchUserId);
		if(branchUsers!=null&& ValidateUtils.isIdentity(branchUsers.getIdentity(), UserIdentityEnums.branch)){
			UBranches branches=branchesMapper.selectByPrimaryKey(branchUserId);
			if(branches!=null){
				param.setBranchuserid(branchUserId);
				param.setAgentuserid(branches.getAgentuserid());
				param.setCreatetime(new Date());
				UUsers customerUsers=usersMapper.getUUsersByPhone(param.getPhone());
				if(customerUsers!=null){
					param.setUserid(customerUsers.getUserid()); 
				}
				customerMapper.insert(param);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("�����ɹ�");
				return rq;
			} 
		}
		rq.setStatusreson("����������ѽ�ĺ�����飬�˹��ܵ�Ȩ�޲��㣡");  
		return rq;
	}
	
	

}
