package com.bbyiya.service.impl.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.TiAgentsMapper;
import com.bbyiya.dao.TiAgentsapplyMapper;
import com.bbyiya.dao.TiMachinemodelMapper;
import com.bbyiya.dao.TiMachineproductsMapper;
import com.bbyiya.dao.TiProducerapplymachinesMapper;
import com.bbyiya.dao.TiProducerproductsMapper;
import com.bbyiya.dao.TiProducersMapper;
import com.bbyiya.dao.TiProducersapplyMapper;
import com.bbyiya.dao.TiProductareasMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.TiPromotersapplyMapper;
import com.bbyiya.dao.UAccountsMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ProducersStatusEnum;
import com.bbyiya.enums.calendar.PromoterStatusEnum;
import com.bbyiya.enums.calendar.TiAgentStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.TiAgents;
import com.bbyiya.model.TiAgentsapply;
import com.bbyiya.model.TiMachinemodel;
import com.bbyiya.model.TiMachineproducts;
import com.bbyiya.model.TiProducerapplymachines;
import com.bbyiya.model.TiProducerproducts;
import com.bbyiya.model.TiProducers;
import com.bbyiya.model.TiProducersapply;
import com.bbyiya.model.TiProductareas;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.model.UAccounts;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.service.calendar.ITi_AgentMgtService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.RAreaVo;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiAgentApplyVo;
import com.bbyiya.vo.calendar.TiAgentSearchParam;
import com.bbyiya.vo.calendar.TiMachineproductVo;
import com.bbyiya.vo.calendar.TiProducersApplyVo;
import com.bbyiya.vo.calendar.TiPromoterApplyVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("ti_AgentMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ti_AgentMgtServiceImpl implements ITi_AgentMgtService{
	
	@Autowired
	private TiAgentsapplyMapper agentapplyMapper;
	@Autowired
	private TiAgentsMapper agentsMapper;	
	@Autowired
	private TiPromotersapplyMapper promoterapplyMapper;	
	@Autowired
	private TiPromotersMapper promoterMapper;
	@Autowired
	private TiPromoteremployeesMapper promoteremployeeMapper;
	
	@Autowired
	private TiProducersapplyMapper producersapplyMapper;
	@Autowired
	private TiProducerproductsMapper producerproductMapper;
	
	@Autowired
	private TiProducersMapper producersMapper;
	@Autowired
	private TiMachinemodelMapper machineMapper;
	@Autowired
	private TiMachineproductsMapper machineproductMapper;
	@Autowired
	private TiProducerapplymachinesMapper promachineMapper;
	@Autowired
	private TiProductareasMapper productareaMapper;
	
	/***************************产品*********************************/
	@Autowired
	private TiProductsMapper tiproductMapper;
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UAccountsMapper accountsMapper;
	//用户公共模块
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	
	/**
	 * 推广代理商申请
	 */
	public ReturnModel tiagentApply(Long userId,TiAgentsapply applyInfo){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		TiAgentsapply apply= agentapplyMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			applyInfo.setStatus(apply.getStatus());
			applyInfo.setAgentuserid(userId);
		}
		if(applyInfo==null){
			rq.setStatusreson("参数有误");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getCompanyname())){
			rq.setStatusreson("公司名称不能为空");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getContacts())){
			rq.setStatusreson("联系人必须填");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getMobilephone())){
			rq.setStatusreson("手机号必须填");
			return rq;
		} 
		if(!ObjectUtil.isMobile(applyInfo.getMobilephone())){
			rq.setStatusreson("请输入正确的手机号");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getProvince())||ObjectUtil.isEmpty(applyInfo.getCity())||ObjectUtil.isEmpty(applyInfo.getArea())){
			rq.setStatusreson("请选择省市区！");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getStreetdetail())){
			rq.setStatusreson("地址必须填");
			return rq;
		} 
		
		if(!ObjectUtil.validSqlStr(applyInfo.getCompanyname())
				||!ObjectUtil.validSqlStr(applyInfo.getContacts())
				||!ObjectUtil.validSqlStr(applyInfo.getStreetdetail())
				||!ObjectUtil.validSqlStr(applyInfo.getMobilephone())
				||(!ObjectUtil.isEmpty(applyInfo.getRemark())&&!ObjectUtil.validSqlStr(applyInfo.getRemark()))
				){
			rq.setStatusreson("存在非法字符");
			return rq;
		}
		applyInfo.setAgentuserid(userId);
		applyInfo.setCreatetime(new Date());
		
		TiPromoters promoters=promoterMapper.selectByPrimaryKey(userId);		
		if(promoters!=null&&promoters.getStatus()!=null&&promoters.getStatus().intValue()==Integer.parseInt(PromoterStatusEnum.ok.toString())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("你已经为活动参与单位，不能再申请推广代理商！");
			return rq;
		}
		
		if(apply!=null&&applyInfo.getAgentuserid()!=null&&applyInfo.getAgentuserid()>0){
			//如果是已通过审核的代理商
			if(applyInfo.getStatus()!=null&&applyInfo.getStatus()==Integer.parseInt(TiAgentStatusEnum.ok.toString())){
				//如是是正式代理商，同步更新
				TiAgents agentModel=agentsMapper.selectByPrimaryKey(apply.getAgentuserid());
				if(agentModel!=null){
					agentModel.setCompanyname(applyInfo.getCompanyname());
					agentModel.setContacts(applyInfo.getContacts());
					agentModel.setMobilephone(applyInfo.getMobilephone());
					agentsMapper.updateByPrimaryKeySelective(agentModel);
				}
			}else{
				applyInfo.setStatus(Integer.parseInt(TiAgentStatusEnum.applying.toString()));  
			}
			agentapplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			applyInfo.setStatus(Integer.parseInt(TiAgentStatusEnum.applying.toString()));  
			agentapplyMapper.insert(applyInfo);
			
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("推广代理商资质提交成功！"); 
		return rq;
	}
	
	
	/**
	 * 推广者申请
	 */
	public ReturnModel applyPromoter(Long userId,TiPromotersapply applyInfo){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		TiPromotersapply apply= promoterapplyMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			applyInfo.setStatus(apply.getStatus());
		}
		if(applyInfo==null){
			rq.setStatusreson("参数有误");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getAgentuserid())){
			rq.setStatusreson("代理商ID号不能为空");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getCompanyname())){
			rq.setStatusreson("公司名称不能为空");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getContacts())){
			rq.setStatusreson("联系人必须填");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getMobilephone())){
			rq.setStatusreson("手机号必须填");
			return rq;
		} 
		if(!ObjectUtil.isMobile(applyInfo.getMobilephone())){
			rq.setStatusreson("请输入正确的手机号");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getProvince())||ObjectUtil.isEmpty(applyInfo.getCity())||ObjectUtil.isEmpty(applyInfo.getArea())){
			rq.setStatusreson("请选择省市区！");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getStreetdetails())){
			rq.setStatusreson("地址必须填");
			return rq;
		} 
		
		if(!ObjectUtil.validSqlStr(applyInfo.getCompanyname())
				||!ObjectUtil.validSqlStr(applyInfo.getContacts())
				||!ObjectUtil.validSqlStr(applyInfo.getStreetdetails())
				||!ObjectUtil.validSqlStr(applyInfo.getMobilephone())
				||(!ObjectUtil.isEmpty(applyInfo.getRemark())&&!ObjectUtil.validSqlStr(applyInfo.getRemark()))
				){
			rq.setStatusreson("存在非法字符");
			return rq;
		}
		applyInfo.setPromoteruserid(userId);
		applyInfo.setCreattime(new Date());
		TiAgentsapply belongagents=agentapplyMapper.selectByPrimaryKey(applyInfo.getAgentuserid());
		if(belongagents==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("推广代理商咿呀号不存在！");
			return rq;
		}
		if(belongagents.getStatus()!=Integer.parseInt(TiAgentStatusEnum.ok.toString())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("推广代理商咿呀号未通过审核或还在审核中！");
			return rq;
		}
		TiAgents agents=agentsMapper.selectByPrimaryKey(userId);		
		if(agents!=null&&agents.getStatus()!=null&&agents.getStatus().intValue()==Integer.parseInt(TiAgentStatusEnum.ok.toString())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("你已经为推广代理商身份，不能再申请为活动参与单位！");
			return rq;
		}
		
		if(apply!=null){
			//如果是已通过审核的推广者
			if(applyInfo.getStatus()!=null&&applyInfo.getStatus()==Integer.parseInt(PromoterStatusEnum.ok.toString())){
				//如是是正式推广者，同步更新
				TiPromoters promoterModel=promoterMapper.selectByPrimaryKey(apply.getPromoteruserid());
				if(promoterModel!=null){
					promoterModel.setCompanyname(applyInfo.getCompanyname());
					promoterModel.setContacts(applyInfo.getContacts());
					promoterModel.setMobilephone(applyInfo.getMobilephone());
					promoterModel.setArea(applyInfo.getArea());
					promoterModel.setCity(applyInfo.getCity());
					promoterModel.setProvince(applyInfo.getProvince());
					promoterModel.setStreetdetails(applyInfo.getStreetdetails());
					promoterModel.setRemark(applyInfo.getRemark());
					promoterMapper.updateByPrimaryKeySelective(promoterModel);
				}
			}else{
				applyInfo.setStatus(Integer.parseInt(PromoterStatusEnum.applying.toString()));  
			}
			promoterapplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			applyInfo.setStatus(Integer.parseInt(PromoterStatusEnum.applying.toString()));  
			promoterapplyMapper.insert(applyInfo);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("活动参与单位资质提交成功！"); 
		return rq;
	}
	
	
	/**
	 * 生产商申请
	 */
	public ReturnModel applyProducers(Long userId,TiProducersapply applyInfo,List<TiMachinemodel> machinelist){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		TiProducersapply apply= producersapplyMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			applyInfo.setStatus(apply.getStatus());
		}
		if(applyInfo==null){
			rq.setStatusreson("参数有误");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getCompanyname())){
			rq.setStatusreson("公司名称不能为空");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getContacts())){
			rq.setStatusreson("联系人必须填");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getMobilephone())){
			rq.setStatusreson("手机号必须填");
			return rq;
		} 
		if(!ObjectUtil.isMobile(applyInfo.getMobilephone())){
			rq.setStatusreson("请输入正确的手机号");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getProvince())||ObjectUtil.isEmpty(applyInfo.getCity())||ObjectUtil.isEmpty(applyInfo.getArea())){
			rq.setStatusreson("请选择省市区！");
			return rq;
		} 
		if(ObjectUtil.isEmpty(applyInfo.getStreetdetail())){
			rq.setStatusreson("地址必须填");
			return rq;
		} 
		if(machinelist==null||machinelist.size()<=0){
			rq.setStatusreson("请选择生产机型！");
			return rq;
		}
		if(!ObjectUtil.validSqlStr(applyInfo.getCompanyname())
				||!ObjectUtil.validSqlStr(applyInfo.getContacts())
				||!ObjectUtil.validSqlStr(applyInfo.getStreetdetail())
				||!ObjectUtil.validSqlStr(applyInfo.getMobilephone())
				||(!ObjectUtil.isEmpty(applyInfo.getRemark())&&!ObjectUtil.validSqlStr(applyInfo.getRemark()))
				){
			rq.setStatusreson("存在非法字符");
			return rq;
		}
		applyInfo.setProduceruserid(userId);
		applyInfo.setCreatetime(new Date());
		
		//先删除后加入
		List<TiProducerapplymachines> applymachineList=promachineMapper.findapplymachineslist(userId);
		for (TiProducerapplymachines applyma : applymachineList) {
			promachineMapper.deleteByPrimaryKey(applyma.getId());
		}
		for (TiMachinemodel ma : machinelist) {	
			TiProducerapplymachines applyma=new TiProducerapplymachines(); 
			applyma.setMachineid(ma.getMachineid().longValue());
			applyma.setMachinename(ma.getName());
			applyma.setProduceruserid(userId);
			promachineMapper.insert(applyma);
		}
		if(apply!=null&&applyInfo.getProduceruserid()!=null&&applyInfo.getProduceruserid()>0){
			//如果是已通过审核的生产商
			if(applyInfo.getStatus()!=null&&applyInfo.getStatus()==Integer.parseInt(ProducersStatusEnum.ok.toString())){
				//如是是正式生产商，同步更新
				TiProducers producersModel=producersMapper.selectByPrimaryKey(apply.getProduceruserid());
				if(producersModel!=null){
					producersModel.setCompanyname(applyInfo.getCompanyname());
					producersModel.setContacts(applyInfo.getContacts());
					producersModel.setMobilephone(applyInfo.getMobilephone());
					producersMapper.updateByPrimaryKeySelective(producersModel);
				}
				
				//先删除后添加
				List<TiProducerproducts> proproducts=producerproductMapper.findTiProducerproductsByProducerUserId(apply.getProduceruserid());
				if(proproducts!=null&&proproducts.size()>0){
					for (TiProducerproducts tiProducerproducts : proproducts) {
						producerproductMapper.deleteByPrimaryKey(tiProducerproducts.getId());
					}
				}
				List<TiProducerapplymachines> applymachines=promachineMapper.findapplymachineslist(apply.getProduceruserid());
				if(applymachines!=null&&applymachines.size()>0){
					for (TiProducerapplymachines applyma : applymachines) {
						List<TiMachineproducts> maproductList=this.findProductListByMachineId(applyma.getMachineid().intValue());
						for (TiMachineproducts product : maproductList) {
							TiProducerproducts  proprodcut=new TiProducerproducts();
							proprodcut.setProduceruserid(applyma.getProduceruserid());
							proprodcut.setProductid(product.getProductid());
							producerproductMapper.insert(proprodcut);
						}
					}
				}
			}else{
				applyInfo.setStatus(Integer.parseInt(ProducersStatusEnum.applying.toString()));  
			}
			producersapplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			applyInfo.setStatus(Integer.parseInt(ProducersStatusEnum.applying.toString()));  
			producersapplyMapper.insert(applyInfo);		
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("授权生产商资质提交成功！"); 
		return rq;
	}
	
	
	/**
	 * 代理商审核
	 */
	public ReturnModel audit_AgentApply(Long adminId,Long agentUserId,int status,String msg){
		ReturnModel rq=new ReturnModel();
		TiAgentsapply apply= agentapplyMapper.selectByPrimaryKey(agentUserId); 
		if(apply!=null){
			if(status==Integer.parseInt(TiAgentStatusEnum.ok.toString())){//成为代理商
				//代理商 申请信息复制到正式代理表
				this.addAgentInfo(apply);
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("审核成功");
				apply.setStatus(Integer.parseInt(TiAgentStatusEnum.ok.toString())); 
//				//给客户咿呀绑定的手机号发送短信
//				UUsers user=usersMapper.getUUsersByUserID(apply.getAgentuserid());
//				if(!ObjectUtil.isEmpty(user.getMobilephone())){
//					SmsParam sendparam=new SmsParam();
//					sendparam.setUserId(user.getUserid());
//					SendSMSByMobile.sendSmS(Integer.parseInt(SendMsgEnums.agentApply_pass.toString()),user.getMobilephone(), sendparam);					
//				}
			
			}else{
				apply.setStatus(Integer.parseInt(TiAgentStatusEnum.no.toString())); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("拒绝成功");
			}
			apply.setProcesstime(new Date());//处理时间
			apply.setReason(msg);
			agentapplyMapper.updateByPrimaryKeySelective(apply);
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到申请资料");
		} 
		return rq;
	}
	
	
	/**
	 * 代理商通过审核，录入代理商信息、代理商身份标识
	 * @param apply
	 */
	public void addAgentInfo(TiAgentsapply apply){
		if(apply!=null){
			//代理商录入
			TiAgents agentModel=agentsMapper.selectByPrimaryKey(apply.getAgentuserid());
			boolean isadd=false;
			if(agentModel==null){
				agentModel=new TiAgents();
				isadd=true;
			}
			agentModel.setAgentuserid(apply.getAgentuserid());
			agentModel.setCompanyname(apply.getCompanyname());
			agentModel.setContacts(apply.getContacts());
			agentModel.setMobilephone(apply.getMobilephone());
			agentModel.setStatus(Integer.parseInt(TiAgentStatusEnum.ok.toString()));
			if(isadd){
				agentsMapper.insertSelective(agentModel);
			}else{
				agentsMapper.updateByPrimaryKey(agentModel);
			}
			//更新代理身份标识 预留
			userBasic.addUserIdentity(apply.getAgentuserid(),UserIdentityEnums.ti_agent); 
		}
	}
	
	/**
	 * 推广者审核
	 */
	public ReturnModel audit_PromoterApply(Long adminId,Long promoterUserId,int status,String msg){
		ReturnModel rq=new ReturnModel();
		TiPromotersapply apply= promoterapplyMapper.selectByPrimaryKey(promoterUserId); 
		if(apply!=null){
			if(status==Integer.parseInt(PromoterStatusEnum.ok.toString())){//成为代理商
				//代理商 申请信息复制到正式代理表
				this.addPromoterInfo(apply);
				apply.setStatus(Integer.parseInt(PromoterStatusEnum.ok.toString())); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("审核成功");
			}else{
				apply.setStatus(Integer.parseInt(PromoterStatusEnum.no.toString())); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("拒绝成功");
			}
			apply.setProcesstime(new Date());//处理时间
			apply.setReason(msg);
			promoterapplyMapper.updateByPrimaryKeySelective(apply);
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到申请资料");
		} 
		return rq;
	}
	
	/**
	 * 推广者通过审核，录入推广者信息、推广者身份标识
	 * @param apply
	 */
	public void addPromoterInfo(TiPromotersapply apply){
		if(apply!=null){
			//代理商录入
			TiPromoters promoterModel=promoterMapper.selectByPrimaryKey(apply.getPromoteruserid());
			boolean isadd=false;
			if(promoterModel==null){
				promoterModel=new TiPromoters();
				isadd=true;
			}
			promoterModel.setPromoteruserid(apply.getPromoteruserid());
			promoterModel.setAgentuserid(apply.getAgentuserid());
			promoterModel.setCompanyname(apply.getCompanyname());
			promoterModel.setContacts(apply.getContacts());
			promoterModel.setMobilephone(apply.getMobilephone());
			promoterModel.setProvince(apply.getProvince());
			promoterModel.setCity(apply.getCity());
			promoterModel.setArea(apply.getArea());
			promoterModel.setStreetdetails(apply.getStreetdetails());
			promoterModel.setRemark(apply.getRemark());
			promoterModel.setStatus(Integer.parseInt(PromoterStatusEnum.ok.toString()));
			if(isadd){
				promoterMapper.insertSelective(promoterModel);
			}else{
				promoterMapper.updateByPrimaryKey(promoterModel);
			}
			//更新代理身份标识 预留
			userBasic.addUserIdentity(apply.getPromoteruserid(),UserIdentityEnums.ti_promoter); 
			
			TiPromoteremployees employee=promoteremployeeMapper.selectByPrimaryKey(promoterModel.getPromoteruserid());
			if(employee==null){
				employee=new TiPromoteremployees();
				employee.setCreatetime(new Date());
				employee.setName(apply.getContacts());
				employee.setPromoteruserid(apply.getPromoteruserid());
				employee.setUserid(apply.getPromoteruserid());
				employee.setStatus(Integer.parseInt(PromoterStatusEnum.ok.toString()));
				promoteremployeeMapper.insert(employee);
				//更新代理身份标识 预留
				userBasic.addUserIdentity(apply.getPromoteruserid(),UserIdentityEnums.ti_employees); 
				
			}else{
				employee.setStatus(Integer.parseInt(PromoterStatusEnum.ok.toString()));
				promoteremployeeMapper.updateByPrimaryKey(employee);
			}
			
			
		}
	}
	
	
	/**
	 * 生产者审核
	 */
	public ReturnModel audit_ProducersApply(Long adminId,Long producersUserId,int status,String msg){
		ReturnModel rq=new ReturnModel();
		TiProducersapply apply= producersapplyMapper.selectByPrimaryKey(producersUserId); 
		if(apply!=null){
			if(status==Integer.parseInt(ProducersStatusEnum.ok.toString())){//成为代理商
				//代理商 申请信息复制到正式代理表
				this.addProducersInfo(apply);
				apply.setStatus(Integer.parseInt(ProducersStatusEnum.ok.toString())); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("审核成功");
			}else{
				apply.setStatus(Integer.parseInt(ProducersStatusEnum.no.toString())); 
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("拒绝成功");
			}
			apply.setProcesstime(new Date());//处理时间
			apply.setReason(msg);
			producersapplyMapper.updateByPrimaryKeySelective(apply);
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到申请资料");
		} 
		return rq;
	}
	
	
	/**
	 * 生产商通过审核，录入生产商信息、生产商身份标识
	 * @param apply
	 */
	public void addProducersInfo(TiProducersapply apply){
		if(apply!=null){
			//代理商录入
			TiProducers producersModel=producersMapper.selectByPrimaryKey(apply.getProduceruserid());
			boolean isadd=false;
			if(producersModel==null){
				producersModel=new TiProducers();
				isadd=true;
			}
			producersModel.setProduceruserid(apply.getProduceruserid());
			producersModel.setCompanyname(apply.getCompanyname());
			producersModel.setContacts(apply.getContacts());
			producersModel.setMobilephone(apply.getMobilephone());
			producersModel.setStatus(Integer.parseInt(ProducersStatusEnum.ok.toString()));
			if(isadd){
				producersMapper.insertSelective(producersModel);
			}else{
				producersMapper.updateByPrimaryKey(producersModel);
			}
			
			//先删除后添加
			List<TiProducerproducts> proproducts=producerproductMapper.findTiProducerproductsByProducerUserId(apply.getProduceruserid());
			if(proproducts!=null&&proproducts.size()>0){
				for (TiProducerproducts tiProducerproducts : proproducts) {
					producerproductMapper.deleteByPrimaryKey(tiProducerproducts.getId());
				}
			}
			List<TiProducerapplymachines> applymachines=promachineMapper.findapplymachineslist(apply.getProduceruserid());
			if(applymachines!=null&&applymachines.size()>0){
				for (TiProducerapplymachines applyma : applymachines) {
					List<TiMachineproducts> maproductList=this.findProductListByMachineId(applyma.getMachineid().intValue());
					for (TiMachineproducts product : maproductList) {
						TiProducerproducts  proprodcut=new TiProducerproducts();
						proprodcut.setProduceruserid(applyma.getProduceruserid());
						proprodcut.setProductid(product.getProductid());
						producerproductMapper.insert(proprodcut);
					}
				}
			}
			
			
			//更新代理身份标识 预留
			userBasic.addUserIdentity(apply.getProduceruserid(),UserIdentityEnums.ti_producer); 
		}
	}
	
	public ReturnModel getAgentApplyStatusModel(Long agentUserId){
		Map<String, Object> map=new HashMap<String, Object>();
		TiAgentApplyVo agentapply= agentapplyMapper.getUAgentapplyVOByAgentUserId(agentUserId);
		if(agentapply!=null){
			map.put("isApplyed", 1);
			map.put("status", agentapply.getStatus());
			agentapply.setProvinceName(regionService.getProvinceName(agentapply.getProvince())) ;
			agentapply.setCityName(regionService.getCityName(agentapply.getCity())) ;
			agentapply.setAreaName(regionService.getAresName(agentapply.getArea())) ;
			
			if(agentapply.getStatus()!=null){
				if(agentapply.getStatus().intValue()==Integer.parseInt(TiAgentStatusEnum.ok.toString())){
					map.put("msg", "审核已通过，已成为推广代理商");
				}else if (agentapply.getStatus().intValue()==Integer.parseInt(TiAgentStatusEnum.applying.toString())) {
					map.put("msg", "申请中");
				}else if (agentapply.getStatus().intValue()==Integer.parseInt(TiAgentStatusEnum.no.toString())) {
					map.put("msg", "申请不通过");
				}
			}else {
				map.put("msg", "申请中");
			}
			map.put("applyInfo", agentapply);
		}else {
			map.put("isApplyed", 0);
		}
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	public ReturnModel getPromoterApplyStatusModel(Long promoterUserId){
		Map<String, Object> map=new HashMap<String, Object>();
		TiPromoterApplyVo promoterapply= promoterapplyMapper.getTiPromoterapplyVOById(promoterUserId);
		if(promoterapply!=null){
			map.put("isApplyed", 1);
			map.put("status", promoterapply.getStatus());
			promoterapply.setProvinceName(regionService.getProvinceName(promoterapply.getProvince())) ;
			promoterapply.setCityName(regionService.getCityName(promoterapply.getCity())) ;
			promoterapply.setAreaName(regionService.getAresName(promoterapply.getArea())) ;
			
			if(promoterapply.getStatus()!=null){
				if(promoterapply.getStatus().intValue()==Integer.parseInt(PromoterStatusEnum.ok.toString())){
					map.put("msg", "审核已通过，已成为活动参与单位");
				}else if (promoterapply.getStatus().intValue()==Integer.parseInt(PromoterStatusEnum.applying.toString())) {
					map.put("msg", "申请中");
				}else if (promoterapply.getStatus().intValue()==Integer.parseInt(PromoterStatusEnum.no.toString())) {
					map.put("msg", "申请不通过");
				}
			}else {
				map.put("msg", "申请中");
			}
			map.put("applyInfo", promoterapply);
		}else {
			map.put("isApplyed", 0);
		}
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	public ReturnModel getproducersApplyStatusModel(Long producersUserId){
		Map<String, Object> map=new HashMap<String, Object>();
		TiProducersApplyVo producersapply= producersapplyMapper.getTiProducersapplyVOById(producersUserId);
		if(producersapply!=null){
			map.put("isApplyed", 1);
			map.put("status", producersapply.getStatus());
			producersapply.setProvinceName(regionService.getProvinceName(producersapply.getProvince())) ;
			producersapply.setCityName(regionService.getCityName(producersapply.getCity())) ;
			producersapply.setAreaName(regionService.getAresName(producersapply.getArea())) ;
			
			List<TiProducerapplymachines> machines=promachineMapper.findapplymachineslist(producersapply.getProduceruserid());
			if(machines!=null&&machines.size()>0){
				producersapply.setMachines(machines);
			}
			if(producersapply.getStatus()!=null){
				if(producersapply.getStatus().intValue()==Integer.parseInt(ProducersStatusEnum.ok.toString())){
					map.put("msg", "审核已通过，已成为授权生产商");
				}else if (producersapply.getStatus().intValue()==Integer.parseInt(ProducersStatusEnum.applying.toString())) {
					map.put("msg", "审核中");
				}else if (producersapply.getStatus().intValue()==Integer.parseInt(ProducersStatusEnum.no.toString())) {
					map.put("msg", "审核未通过");
				}
			}else {
				map.put("msg", "审核中");
			}
			map.put("applyInfo", producersapply);
		}else {
			map.put("isApplyed", 0);
		}
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	/**
	 * 得到代理商申请列表
	 */
	public ReturnModel findTiAgentApplyList(TiAgentSearchParam param,int index,int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		List<TiAgentApplyVo> list=agentapplyMapper.findTiAgentapplyVOList(param);
		PageInfo<TiAgentApplyVo> result=new PageInfo<TiAgentApplyVo>(list);
		for (TiAgentApplyVo agentvo : result.getList()) {
			agentvo.setProvinceName(regionService.getProvinceName(agentvo.getProvince())) ;
			agentvo.setCityName(regionService.getCityName(agentvo.getCity())) ;
			agentvo.setAreaName(regionService.getAresName(agentvo.getArea())) ;
			UUsers user=usersMapper.getUUsersByUserID(agentvo.getAgentuserid());
			if(user!=null){
				agentvo.setBindphone(user.getMobilephone());
			}
			UAccounts account=accountsMapper.selectByPrimaryKey(agentvo.getAgentuserid());
			if(account!=null){
				agentvo.setAvailableAmount(account.getAvailableamount());
			}else{
				agentvo.setAvailableAmount(0.0);
			}
		}
		rq.setBasemodle(result);
		return rq;
	}
	
	/**
	 * 得到推广者申请列表
	 */
	public ReturnModel findTiPromoterApplyList(TiAgentSearchParam param,int index,int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		List<TiPromoterApplyVo> list=promoterapplyMapper.findTiPromoterapplyVOList(param);
		PageInfo<TiPromoterApplyVo> result=new PageInfo<TiPromoterApplyVo>(list);
		for (TiPromoterApplyVo agentvo : result.getList()) {
			agentvo.setProvinceName(regionService.getProvinceName(agentvo.getProvince())) ;
			agentvo.setCityName(regionService.getCityName(agentvo.getCity())) ;
			agentvo.setAreaName(regionService.getAresName(agentvo.getArea())) ;
			UUsers user=usersMapper.getUUsersByUserID(agentvo.getPromoteruserid());
			if(user!=null){
				agentvo.setBindphone(user.getMobilephone());
			}
			UAccounts account=accountsMapper.selectByPrimaryKey(agentvo.getPromoteruserid());
			if(account!=null){
				agentvo.setAvailableAmount(account.getAvailableamount());
			}else{
				agentvo.setAvailableAmount(0.0);
			}
				
		}
		rq.setBasemodle(result);
		return rq;
	}
	
	
	/**
	 * 得到生产商申请列表
	 */
	public ReturnModel findTiProducersApplyList(TiAgentSearchParam param,int index,int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		List<TiProducersApplyVo> list=producersapplyMapper.findTiProducersapplyVOList(param);
		PageInfo<TiProducersApplyVo> result=new PageInfo<TiProducersApplyVo>(list);
		for (TiProducersApplyVo producersvo : result.getList()) {
			producersvo.setProvinceName(regionService.getProvinceName(producersvo.getProvince())) ;
			producersvo.setCityName(regionService.getCityName(producersvo.getCity())) ;
			producersvo.setAreaName(regionService.getAresName(producersvo.getArea())) ;
			UUsers user=usersMapper.getUUsersByUserID(producersvo.getProduceruserid());
			if(user!=null){
				producersvo.setBindphone(user.getMobilephone());
			}
			UAccounts account=accountsMapper.selectByPrimaryKey(producersvo.getProduceruserid());
			if(account!=null){
				producersvo.setAvailableAmount(account.getAvailableamount());
			}else{
				producersvo.setAvailableAmount(0.0);
			}
			List<TiProducerapplymachines> machines=promachineMapper.findapplymachineslist(producersvo.getProduceruserid());
			if(machines!=null&&machines.size()>0){
				producersvo.setMachines(machines);
			}	
		}
		rq.setBasemodle(result);
		return rq;
	}
	
	/**
	 * 获取代理商信息
	 * @param agentUserId
	 * @return
	 */
	public TiAgentApplyVo getTiAgentsInfo(Long agentUserId){	
		//加入缓存半个小时
		String keyString="tiagentsvo_"+agentUserId;
		TiAgentApplyVo tiagent=(TiAgentApplyVo) RedisUtil.getObject(keyString);
		if(tiagent==null){
			tiagent=agentapplyMapper.getUAgentapplyVOByAgentUserId(agentUserId);	
			if(tiagent!=null){
				tiagent.setProvinceName(regionService.getProvinceName(tiagent.getProvince())) ;
				tiagent.setCityName(regionService.getCityName(tiagent.getCity())) ;
				tiagent.setAreaName(regionService.getAresName(tiagent.getArea())) ;	
			}
			RedisUtil.setObject(keyString, tiagent, 1800);
		}		
		return tiagent;		
	}
	
	
	/**
	 * 获取推广者代理信息
	 * @param agentUserId
	 * @return
	 */
	public TiPromoterApplyVo getTiPromoterInfo(Long promoterUserId){	
		//加入缓存半个小时
		String keyString="tipromotervo_"+promoterUserId;
		TiPromoterApplyVo tipromoter=(TiPromoterApplyVo) RedisUtil.getObject(keyString);
		if(tipromoter==null){
			tipromoter=promoterapplyMapper.getTiPromoterapplyVOById(promoterUserId);	
			if(tipromoter!=null){
				tipromoter.setProvinceName(regionService.getProvinceName(tipromoter.getProvince())) ;
				tipromoter.setCityName(regionService.getCityName(tipromoter.getCity())) ;
				tipromoter.setAreaName(regionService.getAresName(tipromoter.getArea())) ;	
			}
			if(tipromoter!=null){
				TiAgents tiagent=agentsMapper.selectByPrimaryKey(tipromoter.getAgentuserid());
				if(tiagent!=null){
					tipromoter.setAgentName(tiagent.getCompanyname());
				}
			}
			RedisUtil.setObject(keyString, tipromoter, 1800);
		}		
		return tipromoter;		
	}
	
	
	
	/**
	 * 获取生产商代理信息
	 * @param agentUserId
	 * @return
	 */
	public TiProducersApplyVo getTiProducerInfo(Long producerUserId){	
		//加入缓存半个小时
		String keyString="tiproducervo_"+producerUserId;
		TiProducersApplyVo tiproducer=(TiProducersApplyVo) RedisUtil.getObject(keyString);
		if(tiproducer==null){
			tiproducer=producersapplyMapper.getTiProducersapplyVOById(producerUserId);	
			if(tiproducer!=null){
				tiproducer.setProvinceName(regionService.getProvinceName(tiproducer.getProvince())) ;
				tiproducer.setCityName(regionService.getCityName(tiproducer.getCity())) ;
				tiproducer.setAreaName(regionService.getAresName(tiproducer.getArea())) ;	
			}
			RedisUtil.setObject(keyString, tiproducer, 1800);
		}
		List<TiProducerapplymachines> machines=promachineMapper.findapplymachineslist(producerUserId);
		if(tiproducer!=null){
			tiproducer.setMachines(machines);
		}
		
		return tiproducer;		
	}
	
	
	/**
	 * 修改代理商收货地址
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel editTiAgentsAddress(Long agentUserId,String streetdetail,String name,String phone){	
		ReturnModel rqModel=new ReturnModel();
		TiAgentsapply agentsapply=agentapplyMapper.selectByPrimaryKey(agentUserId);
		TiAgents agents=agentsMapper.selectByPrimaryKey(agentUserId);
		if(agents!=null){
			agents.setContacts(name);
			agents.setMobilephone(phone);
			agentsMapper.updateByPrimaryKeySelective(agents);
		}
		if(agentsapply!=null){
			agentsapply.setStreetdetail(streetdetail);
			agentsapply.setContacts(name);
			agentsapply.setMobilephone(phone);
			agentapplyMapper.updateByPrimaryKey(agentsapply);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("修改收货地址成功！");
		return rqModel;		
	}
	
	/**
	 * 修改代理商收货地址
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel editTiPromotersAddress(Long promoteruserid,String streetdetail,String name,String phone){	
		ReturnModel rqModel=new ReturnModel();
		TiPromotersapply promoterapply=promoterapplyMapper.selectByPrimaryKey(promoteruserid);
		TiPromoters promoter=promoterMapper.selectByPrimaryKey(promoteruserid);
		if(promoterapply!=null){
			promoterapply.setStreetdetails(streetdetail);
			promoterapply.setContacts(name);
			promoterapply.setMobilephone(phone);
			promoterapplyMapper.updateByPrimaryKeySelective(promoterapply);
		}
		if(promoter!=null){
			promoter.setStreetdetails(streetdetail);
			promoter.setContacts(name);
			promoter.setMobilephone(phone);
			promoterMapper.updateByPrimaryKeySelective(promoter);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("修改收货地址成功！");
		return rqModel;		
	}
	
	/**
	 * 修改生产商收货地址
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel editTiProducerAddress(Long produceruserid,String streetdetail,String name,String phone){	
		ReturnModel rqModel=new ReturnModel();
		TiProducersapply producerapply=producersapplyMapper.selectByPrimaryKey(produceruserid);
		TiProducers producer=producersMapper.selectByPrimaryKey(produceruserid);
		if(producerapply!=null){
			producerapply.setStreetdetail(streetdetail);
			producerapply.setContacts(name);
			producerapply.setMobilephone(phone);
			producersapplyMapper.updateByPrimaryKeySelective(producerapply);
		}
		if(producer!=null){
			producer.setContacts(name);
			producer.setMobilephone(phone);
			producersMapper.updateByPrimaryKeySelective(producer);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("修改收货地址成功！");
		return rqModel;		
	}

	
	public ReturnModel findMachinemodelList(){
		ReturnModel rqModel=new ReturnModel();
		List<TiMachinemodel> list=machineMapper.findMachineModelList();
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("list", list);
		rqModel.setBasemodle(map);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("获取列表成功！");
		return rqModel;	
	}
	

	public ReturnModel findMachineListByProducerUserId(Long producerUserId){
		ReturnModel rqModel=new ReturnModel();
		List<TiProducerapplymachines> list=promachineMapper.findapplymachineslist(producerUserId);
		for (TiProducerapplymachines applyma : list) {
			List<TiMachineproducts> machineproductList=this.findProductListByMachineId(applyma.getMachineid().intValue());
			List<TiMachineproductVo> listvo=new ArrayList<TiMachineproductVo>();
			for (TiMachineproducts maproduct : machineproductList) {
				TiMachineproductVo vo=new TiMachineproductVo();
				vo.setId(maproduct.getId());
				vo.setMachineid(maproduct.getMachineid());
				vo.setProductid(maproduct.getProductid());
				TiProducts product=tiproductMapper.selectByPrimaryKey(maproduct.getProductid());
				if(product!=null){
					vo.setProducttitle(product.getTitle());
				}
				List<TiProductareas> productarea2=productareaMapper.findProductAreasByProducerUserId(maproduct.getProductid(), producerUserId);
				vo.setSetedareas(productarea2);
				//得到生产商产品不能设置的区域
				List<TiProductareas> productarea=productareaMapper.findProductCannotSetAreas(maproduct.getProductid(), producerUserId);
				vo.setCanotSetareas(productarea);	
				listvo.add(vo);
			}
			applyma.setMachineproduct(listvo);
		}
		//得到生产商信息
		TiProducers producers=producersMapper.selectByPrimaryKey(producerUserId);
		
		
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("list", list);
		map.put("producers", producers);
		
		rqModel.setBasemodle(map);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("获取列表成功！");
		return rqModel;	
	}
	
	
	public List<TiMachineproducts> findProductListByMachineId(Integer machineid){
		List<TiMachineproducts> machineproductList=machineproductMapper.findProductListByMachineId(machineid);
		return machineproductList;
	}
	
	/**
	 * 设置生产商商品生产的地区
	 */
	public ReturnModel setProducerProductAera(Long producerUserId,Long productId,List<RAreaVo> arealist){
		ReturnModel rqModel=new ReturnModel();
		if(arealist==null||arealist.size()<=0){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("请选择要设置的地区！");
			return rqModel;
		}
		TiProducers producer=producersMapper.selectByPrimaryKey(producerUserId);
		if(producer==null||(producer!=null&&producer.getStatus().intValue()!=Integer.parseInt(ProducersStatusEnum.ok.toString()))){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("授权生产商还未审核通过，不能分配地区！");
			return rqModel;
		}
		//得到原有的
		List<TiProductareas> productareas=productareaMapper.findProductAreasByProducerUserId(productId, producerUserId);
		if(productareas!=null&&productareas.size()>0){
			for (TiProductareas area : productareas) {
				boolean ishave=false;
				for (RAreaVo rvo : arealist) {
					if(area.getAreacode().intValue()==rvo.getAreacode().intValue()){
						ishave=true;
						break;
					}
				}
				if(!ishave){
					productareaMapper.deleteByPrimaryKey(area.getId());
				}
				
			}
		}
		for (RAreaVo rvo : arealist) {
			TiProductareas existproductarea=productareaMapper.getIfExistProductAreaByOtherIds(productId, producerUserId,rvo.getAreacode());
			if(existproductarea!=null){
				rqModel.setStatu(ReturnStatus.ParamError);
				rqModel.setStatusreson("地区["+regionService.getAresName(rvo.getAreacode())+"]已被其它生产商代理！");
				return rqModel;
			}
			
			TiProductareas productarea=productareaMapper.getProductAreaByIds(productId, producerUserId,rvo.getAreacode());
			if(productarea==null){
				productarea=new TiProductareas();
				productarea.setAreacode(rvo.getAreacode());
				productarea.setCitycode(rvo.getCitycode());
				productarea.setProvincecode(rvo.getProvincecode());
				productarea.setProduceruserid(producerUserId);
				productarea.setProductid(productId);
				productareaMapper.insert(productarea);
			}
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("设置成功！");
		return rqModel;
	}
}
