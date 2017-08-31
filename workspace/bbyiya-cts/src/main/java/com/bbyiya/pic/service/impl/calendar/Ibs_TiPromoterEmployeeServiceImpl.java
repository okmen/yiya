package com.bbyiya.pic.service.impl.calendar;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.PromoterStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.calendar.IIbs_TiPromoterEmployeeService;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiEmployeeActOffVo;



@Service("ibs_TiPromoterEmployeeService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_TiPromoterEmployeeServiceImpl implements IIbs_TiPromoterEmployeeService{
	
	@Autowired
	private TiPromotersMapper promoterMapper;
	@Autowired
	private TiPromoteremployeesMapper employeeMapper;

	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	//用户公共模块
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	
	public ReturnModel addEmployeeUser(Long promoterUserId,TiEmployeeActOffVo param){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		if(param==null){
			rqModel.setStatusreson("参数有误");
			return rqModel;
		}
	
		//检测影楼身份
		TiPromoters promoter= promoterMapper.selectByPrimaryKey(promoterUserId);
		if(promoter==null||promoter.getStatus()==null||promoter.getStatus().intValue()!=Integer.parseInt(PromoterStatusEnum.ok.toString())){
			rqModel.setStatusreson("您不是影楼管理员，暂时没有权限！");
			return rqModel;
		}
		//检测被添加的用户身份（必须绑定手机）
		UUsers member= usersMapper.getUUsersByPhone(param.getPhone());
		if(member!=null){
			//查看用户是否已经是员工
			TiPromoteremployees employee= employeeMapper.selectByPrimaryKey(member.getUserid()); 
			if(employee!=null){
				//如果不是本影楼的分销人员，依然可以被添加
				if(employee.getPromoteruserid()!=null&&employee.getPromoteruserid().intValue()==promoterUserId){
					rqModel.setStatu(ReturnStatus.ParamError);
					rqModel.setStatusreson("该用户已经是您的员工，不能重复添加！");
					return rqModel;
				}else{
					rqModel.setStatu(ReturnStatus.ParamError);
					rqModel.setStatusreson("该用户已经是其它影楼的员工，不能再添加！");
					return rqModel;
				}
			}
			
			employee=new TiPromoteremployees();
			employee.setPromoteruserid(promoterUserId);
			employee.setCreatetime(new Date());
			employee.setName(param.getName());
			employee.setStatus(1);
			employee.setUserid(param.getUserid());
			employeeMapper.insert(employee);
			
			//被添加的用户的身份标示   预留
			//userBasic.addUserIdentity(member.getUserid(),UserIdentityEnums.salesman); 
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setStatusreson("添加成功！");
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("用户不存在（或者该手机未绑定）！");
		}
		return rqModel;
	}
	
	
}
