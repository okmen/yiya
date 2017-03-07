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
			rqModel.setStatusreson("参数有误");
			return rqModel;
		}
		if(ObjectUtil.isEmpty(param.getPhone())){ 
			rqModel.setStatusreson("手机号不能为空");
			return rqModel;
		}
		//检测影楼身份
		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null||branch.getStatus()==null||branch.getStatus().intValue()!=Integer.parseInt(BranchStatusEnum.ok.toString())){
			rqModel.setStatusreson("您不是影楼管理员，暂时没有权限！");
			return rqModel;
		}
		//检测被添加的用户身份（必须绑定手机）
		UUsers member= usersMapper.getUUsersByPhone(param.getPhone());
		if(member!=null){
			//查看用户是否已经是 分销人员
			UBranchusers usBranchusers= branchusersMapper.selectByPrimaryKey(member.getUserid()); 
			if(usBranchusers!=null){
				//如果不是本影楼的分销人员，依然可以被添加
				if(usBranchusers.getBranchuserid()!=null&&usBranchusers.getBranchuserid().intValue()==branchUserId){
					rqModel.setStatu(ReturnStatus.ParamError);
					rqModel.setStatusreson("该用户已经您的员工，不用重复添加！");
					return rqModel;
				}
			}
			param.setBranchuserid(branchUserId);
			param.setAgentuserid(branch.getAgentuserid());
			param.setUserid(member.getUserid());
			param.setStatus(1);
			param.setCreatetime(new Date());
			branchusersMapper.insert(param);
			
			//被添加的用户的身份标示
			userBasic.addUserIdentity(member.getUserid(),UserIdentityEnums.salesman); 
			
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setStatusreson("添加成功！");
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("用户不存在（或者该手机未绑定）！");
		}
		return rqModel;
	}
	
	public ReturnModel delBranchUser(Long branchUserId,Long userId){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		//检测影楼身份
		UBranches branch= branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch==null||branch.getStatus()==null||branch.getStatus().intValue()!=Integer.parseInt(BranchStatusEnum.ok.toString())){
			rqModel.setStatusreson("您不是影楼管理员，暂时没有权限！");
			return rqModel;
		}
		UBranchusers usBranchusers= branchusersMapper.selectByPrimaryKey(userId); 
		if(usBranchusers!=null){
			if(usBranchusers.getBranchuserid()!=null&&usBranchusers.getBranchuserid().longValue()==branchUserId){
				branchusersMapper.deleteByPrimaryKey(userId);
				//移除用户标识
				userBasic.removeUserIdentity(userId,UserIdentityEnums.salesman); 
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setStatusreson("删除成功！"); 
				
			}
		}
		return rqModel;
	}
	
	

}
