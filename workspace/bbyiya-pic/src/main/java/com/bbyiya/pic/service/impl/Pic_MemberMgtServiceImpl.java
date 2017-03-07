package com.bbyiya.pic.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.BranchStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
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
	
	

}
