package com.bbyiya.pic.service.impl.calendar;

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
import com.bbyiya.dao.TiProducersMapper;
import com.bbyiya.dao.TiProducersapplyMapper;
import com.bbyiya.dao.TiPromoteremployeesMapper;
import com.bbyiya.dao.TiPromotersMapper;
import com.bbyiya.dao.TiPromotersapplyMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.calendar.ProducersStatusEnum;
import com.bbyiya.enums.calendar.PromoterStatusEnum;
import com.bbyiya.enums.calendar.TiAgentStatusEnum;
import com.bbyiya.model.TiAgents;
import com.bbyiya.model.TiAgentsapply;
import com.bbyiya.model.TiProducers;
import com.bbyiya.model.TiProducersapply;
import com.bbyiya.model.TiPromoteremployees;
import com.bbyiya.model.TiPromoters;
import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.calendar.IIbs_TiAgentMgtService;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.TiAgentApplyVo;
import com.bbyiya.vo.calendar.TiAgentSearchParam;
import com.bbyiya.vo.calendar.TiProducersApplyVo;
import com.bbyiya.vo.calendar.TiPromoterApplyVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("ibs_TiAgentMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_TiAgentMgtServiceImpl implements IIbs_TiAgentMgtService{
	
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
	private TiProducersMapper producersMapper;
	
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	//用户公共模块
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	@Resource(name = "regionServiceImpl")
	private IRegionService regionService;
	/**
	 * 代理商申请
	 */
	public ReturnModel tiagentApply(Long userId,TiAgentsapply applyInfo){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);
		TiAgentsapply apply= agentapplyMapper.selectByPrimaryKey(userId); 
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
			rq.setStatusreson("你已经为推广者身份，不能再申请代理商！");
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
			}
			agentapplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			applyInfo.setStatus(Integer.parseInt(TiAgentStatusEnum.applying.toString()));  
			agentapplyMapper.insert(applyInfo);
			
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("代理商资质提交成功！"); 
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
//		if(ObjectUtil.isEmpty(applyInfo.getPromoteruserid())){
//			rq.setStatusreson("生产商ID号不能为空");
//			return rq;
//		}
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
		
		TiAgents agents=agentsMapper.selectByPrimaryKey(userId);		
		if(agents!=null&&agents.getStatus()!=null&&agents.getStatus().intValue()==Integer.parseInt(TiAgentStatusEnum.ok.toString())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("你已经为代理商身份，不能再申请为推广者！");
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
			}
			promoterapplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			applyInfo.setStatus(Integer.parseInt(PromoterStatusEnum.applying.toString()));  
			promoterapplyMapper.insert(applyInfo);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("推广者资质提交成功！"); 
		return rq;
	}
	
	
	/**
	 * 生产商申请
	 */
	public ReturnModel applyProducers(Long userId,TiProducersapply applyInfo){
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
			}
			producersapplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			applyInfo.setStatus(Integer.parseInt(ProducersStatusEnum.applying.toString()));  
			producersapplyMapper.insert(applyInfo);
			
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("生产商资质提交成功！"); 
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
				
//				//给客户咿呀绑定的手机号发送短信
//				UUsers user=usersMapper.getUUsersByUserID(apply.getAgentuserid());
//				if(!ObjectUtil.isEmpty(user.getMobilephone())){
//					SmsParam sendparam=new SmsParam();
//					sendparam.setUserId(user.getUserid());
//					SendSMSByMobile.sendSmS(Integer.parseInt(SendMsgEnums.agentApply_pass.toString()),user.getMobilephone(), sendparam);					
//				}
			
			}else{
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("拒绝成功");
			}
			apply.setStatus(status); 
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
			//userBasic.addUserIdentity(apply.getAgentuserid(),UserIdentityEnums.agent); 
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
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("审核成功");
			}else{
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("拒绝成功");
			}
			apply.setStatus(status); 
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
			promoterModel.setAgentuserid(apply.getAgentuserid());
			promoterModel.setCompanyname(apply.getCompanyname());
			promoterModel.setContacts(apply.getContacts());
			promoterModel.setMobilephone(apply.getMobilephone());
			promoterModel.setStatus(Integer.parseInt(PromoterStatusEnum.ok.toString()));
			if(isadd){
				promoterMapper.insertSelective(promoterModel);
			}else{
				promoterMapper.updateByPrimaryKey(promoterModel);
			}
			//更新代理身份标识 预留
			//userBasic.addUserIdentity(apply.getPromoteruserid(),UserIdentityEnums.agent); 
			
			TiPromoteremployees employee=promoteremployeeMapper.selectByPrimaryKey(promoterModel.getPromoteruserid());
			if(employee==null){
				employee=new TiPromoteremployees();
				employee.setCreatetime(new Date());
				employee.setName(apply.getContacts());
				employee.setPromoteruserid(apply.getPromoteruserid());
				employee.setUserid(apply.getPromoteruserid());
				employee.setStatus(Integer.parseInt(PromoterStatusEnum.ok.toString()));
				promoteremployeeMapper.insert(employee);
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
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("审核成功");
			}else{
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("拒绝成功");
			}
			apply.setStatus(status); 
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
			//更新代理身份标识 预留
			//userBasic.addUserIdentity(apply.getProduceruserid(),UserIdentityEnums.agent); 
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
					map.put("msg", "已经成为代理商");
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
					map.put("msg", "已经成为代理商");
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
			
			if(producersapply.getStatus()!=null){
				if(producersapply.getStatus().intValue()==Integer.parseInt(ProducersStatusEnum.ok.toString())){
					map.put("msg", "已经成为代理商");
				}else if (producersapply.getStatus().intValue()==Integer.parseInt(ProducersStatusEnum.applying.toString())) {
					map.put("msg", "申请中");
				}else if (producersapply.getStatus().intValue()==Integer.parseInt(ProducersStatusEnum.no.toString())) {
					map.put("msg", "申请不通过");
				}
			}else {
				map.put("msg", "申请中");
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
			if(user!=null)
				agentvo.setBindphone(user.getMobilephone());
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
			UUsers user=usersMapper.getUUsersByUserID(agentvo.getAgentuserid());
			if(user!=null)
				agentvo.setBindphone(user.getMobilephone());
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
			if(user!=null)
				producersvo.setBindphone(user.getMobilephone());
		}
		rq.setBasemodle(result);
		return rq;
	}
	
}
